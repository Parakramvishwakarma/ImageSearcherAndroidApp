package com.example.imagesearchandroidapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class RetrieveImagesThread extends Thread {


    private RemoteUtilities remoteUtilities;
    private GetRequestModel requestModel;
    private Activity uiActivity;

    public RetrieveImagesThread(Activity uiActivity, GetRequestModel viewModel) {
        remoteUtilities = RemoteUtilities.getInstance(uiActivity);
        this.requestModel = viewModel;
        this.uiActivity=uiActivity;
    }
    public void run(){
        ArrayList<String> endpoints = getEndpoints(requestModel.getStringResponse());
        if(endpoints.size() == 0){
            uiActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(uiActivity,"No image found",Toast.LENGTH_LONG).show();
                    requestModel.setErrorCode(requestModel.getErrorCode()+1);
                }
            });
        }
        else {
            ArrayList<Bitmap> images = getImagesFromUrls(endpoints);

            try {
                Thread.sleep(3000);
            } catch (Exception e) {
            }
            requestModel.setDownloadedImages(images);
            System.out.println("length of images: " + images.size());
        }
    }

    private ArrayList<String> getEndpoints(String data){
        ArrayList<String> endpoints = new ArrayList<String>();
        try {
            JSONObject jBase = new JSONObject(data);
            JSONArray jHits = jBase.getJSONArray("hits");
            System.out.println(jHits);
            if(jHits.length()< 15 &&  jHits.length() > 0){
                for(int i = 0; i  <  jHits.length(); i ++) {
                    JSONObject jHitsItem = jHits.getJSONObject(i);
                    String url = jHitsItem.getString("largeImageURL");
                    endpoints.add(url);
                }
            }
            else {
                for(int i = 0; i  <  15; i ++) {
                    JSONObject jHitsItem = jHits.getJSONObject(i);
                    String url = jHitsItem.getString("largeImageURL");
                    endpoints.add(url);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return endpoints;
    }

    private ArrayList<Bitmap> getImagesFromUrls(ArrayList<String> imageUrls){
        ArrayList<Bitmap> images = new ArrayList<Bitmap>();
        for(int i = 0; i <imageUrls.size(); i++) {
            Bitmap image = null;
            Uri.Builder url = Uri.parse(imageUrls.get(i)).buildUpon();
            String urlString = url.build().toString();
            HttpURLConnection connection = remoteUtilities.openConnection(urlString);
            if(connection!=null){
                if(remoteUtilities.isConnectionOkay(connection)==true){
                    image = getBitmapFromConnection(connection);
                    connection.disconnect();
                }
            }
            images.add(image);
        }
        return images;
    }

    public Bitmap getBitmapFromConnection(HttpURLConnection conn){
        Bitmap data = null;
        try {
            InputStream inputStream = conn.getInputStream();
            byte[] byteData = getByteArrayFromInputStream(inputStream);
            data = BitmapFactory.decodeByteArray(byteData,0,byteData.length);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }

    private byte[] getByteArrayFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[4096];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

}
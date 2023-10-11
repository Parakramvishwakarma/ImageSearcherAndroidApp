package com.example.imagesearchandroidapp;

import android.app.Activity;
import android.net.Uri;

import java.net.HttpURLConnection;

public class GetRequestThread extends Thread{
    private String searchkey;
    private String baseUrl;
    private RemoteUtilities remoteUtilities;
    private GetRequestModel requestModel;
    public GetRequestThread (String searchKey, Activity uiActivity, GetRequestModel requestModel) {
        this.searchkey = searchKey;
        baseUrl ="https://pixabay.com/api/";
        remoteUtilities = RemoteUtilities.getInstance(uiActivity);
        this.requestModel = requestModel;
    }

    public void run(){
        System.out.println("Hi Runninh");
        String endpoint = getSearchEndpoint();
        HttpURLConnection connection = remoteUtilities.openConnection(endpoint);
        if(connection!=null){
            if(remoteUtilities.isConnectionOkay(connection)==true){
                String response = remoteUtilities.getResponseString(connection);
                connection.disconnect();
                try {
                    Thread.sleep(5000);
                }
                catch (Exception e){

                }
                System.out.println("This is the response : "  + response);
                requestModel.setStringResponse(response);
            }
        }
    }
    private String getSearchEndpoint(){
        String data = null;
        Uri.Builder url = Uri.parse(this.baseUrl).buildUpon();
        url.appendQueryParameter("key","23319229-94b52a4727158e1dc3fd5f2db");
        url.appendQueryParameter("q",this.searchkey);
        String urlString = url.build().toString();
        return urlString;
    }

}

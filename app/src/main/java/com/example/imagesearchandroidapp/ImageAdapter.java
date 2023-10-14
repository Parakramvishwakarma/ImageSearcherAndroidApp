package com.example.imagesearchandroidapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageVH> {
    ArrayList<Bitmap> data;

    UploadImageModel uploadImageModel;


    StorageReference storageRef;

    private Uri filePath;

    public ImageAdapter(ArrayList<Bitmap> data, UploadImageModel model, StorageReference ref){
        this.data = data;
        this.uploadImageModel = model;
        this.storageRef = FirebaseStorage.getInstance().getReference("images/"+"test");

    }
        @NonNull
    @Override
    public ImageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.image_list_item,parent,false);
        return new ImageVH(view, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageVH holder, int position) {
        final boolean[] selected = {false};
        Bitmap imageMap = data.get(position);
        holder.image.setImageBitmap(imageMap);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected[0]) {
                    selected[0] = false;
                    holder.checkBox.setVisibility(View.INVISIBLE);
                    holder.uploadButton.setVisibility(View.GONE);
                }
                else {
                    selected[0] = true;
                    holder.checkBox.setVisibility(View.VISIBLE);
                    holder.uploadButton.setVisibility(View.VISIBLE);
                }
            }});


        holder.uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("uploading now");
//                storageRef.child("images/image.jpg");
//                UploadTask uploadTask = storageRef.putBytes(data);
                Uri test = Uri.parse("http://pixabay.com/get/g2d9208785b8d18b1b1ada11bb9c9ac65d9f5e4a284eee4b05bbb6c899919f39f98d555417f2ffeefe6eece8a709fe9e7bc7d7117b9df17eb857c6c69e02e1f20_1280.jpg");

                storageRef.putFile(test).addOnSuccessListener(taskSnapshot -> {
                    System.out.println("Successs");
                    // Image upload successful
                    // You can get the download URL for the uploaded image
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        System.out.println(downloadUrl);
                        // Now you can use the download URL to display or retrieve the image.
                    });
                }).addOnFailureListener(e -> {
                    System.out.println("Failed: ");
                    e.printStackTrace();
                    // Handle the error in case of a failed upload
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

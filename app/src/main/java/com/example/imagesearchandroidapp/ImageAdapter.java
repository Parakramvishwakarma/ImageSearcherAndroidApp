package com.example.imagesearchandroidapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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

    Context context;

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
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.image_list_item,parent,false);
        return new ImageVH(view, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageVH holder, int position) {
        final boolean[] selected = {false};
        Bitmap imageMap = data.get(position);
        holder.image.setImageBitmap(imageMap);

        Uri imageUri = getImageUri(context, imageMap);
        System.out.println(imageUri);

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
                Uri test = Uri.parse("https://pixabay.com/get/g563aa32da83b490b62962e53fb7be2e0f9f3e5fd64a217c08f51ec1532e541cd75585260ec5c1a3dd0308a5708be22e0cb363ff2d168aded6f46a646f43e7225_640.jpg");

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


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        // Insert the image into the device's MediaStore and get a content URI
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        // Convert the content URI to a Uri object
        return Uri.parse(path);
    }
}

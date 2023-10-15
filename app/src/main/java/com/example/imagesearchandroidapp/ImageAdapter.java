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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImageAdapter extends RecyclerView.Adapter<ImageVH> {
    ArrayList<Bitmap> data;

    UploadImageModel uploadImageModel;

    Context context;

    StorageReference storageRef;


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
        //set the image map
        holder.image.setImageBitmap(imageMap);
        //get each image local Uri
        Uri imageUri = getImageUri(context, imageMap);

        //Render the upload button and the check box on click
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


        //set file name on upload button click and set teh storage ref and ImageUri in the upload model
        holder.uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //we will set the date on the image as the title
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.UK);
                Date now = new Date();
                String fileName = formatter.format(now);
                //create the storage ref for when the image gets uploaded
                storageRef = FirebaseStorage.getInstance().getReference("images/"+fileName);
                uploadImageModel.setImageURI(imageUri);
                uploadImageModel.setRef(storageRef);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


     /* -----------------------------------------------------------------------------------------
            Function: Get local image Uri for image from bitmap
            Author: Parakram
            Reference: https://stackoverflow.com/questions/40885860/how-to-save-bitmap-to-firebase#:~:text=To%20upload%20a%20file%20to,file%2C%20including%20the%20file%20name.&text=Once%20you've%20created%20an,the%20file%20to%20Firebase%20Storage.
     ---------------------------------------------------------------------------------------- */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        // Insert the image into the device's MediaStore and get a content URI
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        // Convert the content URI to a Uri object
        return Uri.parse(path);
    }
}

package com.example.imagesearchandroidapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageVH  extends RecyclerView.ViewHolder{

    ImageButton image;

    ImageView checkBox;

    ImageButton uploadButton;
    public ImageVH(@NonNull View itemView, ViewGroup parent) {
        super(itemView);
        image = itemView.findViewById(R.id.image);
        checkBox = itemView.findViewById(R.id.checkMark);
        uploadButton = itemView.findViewById(R.id.uploadButton);
    }

}

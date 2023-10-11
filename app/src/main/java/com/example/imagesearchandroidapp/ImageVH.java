package com.example.imagesearchandroidapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageVH  extends RecyclerView.ViewHolder{

    ImageButton image;
    public ImageVH(@NonNull View itemView, ViewGroup parent) {
        super(itemView);
        image = itemView.findViewById(R.id.image);
    }
}

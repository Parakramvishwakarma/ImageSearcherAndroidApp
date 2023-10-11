package com.example.imagesearchandroidapp;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageVH> {
    ArrayList<Bitmap> data;

    public ImageAdapter(ArrayList<Bitmap> data){
        this.data = data;
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
        Bitmap imageMap = data.get(position);
        holder.image.setImageBitmap(imageMap);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

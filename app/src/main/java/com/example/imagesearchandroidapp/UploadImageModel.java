package com.example.imagesearchandroidapp;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.net.URI;
import java.util.ArrayList;

public class UploadImageModel  extends ViewModel  {

    public MutableLiveData<URI> imageURI;

    public UploadImageModel(){
        //initial response
        imageURI = new MutableLiveData<URI>();

    }
    public URI getImageURI(){
        return imageURI.getValue();
    }
    public void setImageURI(URI value){
        imageURI.postValue(value);
    }
}

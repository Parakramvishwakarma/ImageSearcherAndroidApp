package com.example.imagesearchandroidapp;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.net.URI;
import java.util.ArrayList;

public class UploadImageModel  extends ViewModel  {

    public MutableLiveData<Integer> imageURI;

    public MutableLiveData<Integer> uploadInt;

    public UploadImageModel(){
        //initial response
        imageURI = new MutableLiveData<Integer>();
        uploadInt = new MutableLiveData<Integer>();
        uploadInt.setValue(0);
    }
    public Integer getImageURI(){
        return imageURI.getValue();
    }
    public void setImageURI(Integer value){
        imageURI.postValue(value);
    }

    public void setUploadInt(int value) {
        uploadInt.postValue(value);
    }

    public int getUploadInt() {
       return uploadInt.getValue();
    }
}

package com.example.imagesearchandroidapp;

import android.graphics.Bitmap;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class GetRequestModel  extends ViewModel  {

    public MutableLiveData<String> stringResponse;
    public MutableLiveData<Integer> errorCode;

    public MutableLiveData<ArrayList<Bitmap>> downloadedImages;
    public GetRequestModel(){
        //initial response
        stringResponse = new MutableLiveData<String>();
        //this is the error code
        errorCode = new MutableLiveData<Integer>();
        errorCode.setValue(0);
        //these are the downlaoded images
        downloadedImages = new MutableLiveData<ArrayList<Bitmap>>();
        ArrayList<Bitmap> temp = new ArrayList<Bitmap>();
        downloadedImages.setValue(temp);
    }

    public String getStringResponse(){
        return stringResponse.getValue();
    }
    public void setStringResponse(String value){
        stringResponse.postValue(value);
    }

    public Integer getErrorCode(){
        return errorCode.getValue();
    }
    public void setErrorCode(Integer value){
        errorCode.postValue(value);
    }
    public ArrayList<Bitmap> getDownloadedImages(){
        return downloadedImages.getValue();
    }
    public void setDownloadedImages(ArrayList<Bitmap> value){
        downloadedImages.postValue(value);
    }
}

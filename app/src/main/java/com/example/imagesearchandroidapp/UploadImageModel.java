package com.example.imagesearchandroidapp;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.util.ArrayList;

public class UploadImageModel  extends ViewModel  {

    //thsi model just has the referece that is needed to upload the image
    public MutableLiveData<StorageReference> ref;

    public MutableLiveData<Uri> imageUri;


    public UploadImageModel(){
        //initial response
        ref = new MutableLiveData<StorageReference>();
        ref.setValue(null);

        imageUri = new MutableLiveData<Uri>();
        imageUri.setValue(null);
    }
    public Uri getImageURI(){
        return imageUri.getValue();
    }
    public void setImageURI(Uri value){
        imageUri.postValue(value);
    }

    public void setRef(StorageReference value) {
        ref.postValue(value);
    }

    public StorageReference getRef() {
        return ref.getValue();
    }

}

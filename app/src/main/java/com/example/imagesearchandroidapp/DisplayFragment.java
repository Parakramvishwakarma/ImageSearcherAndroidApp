package com.example.imagesearchandroidapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.util.ArrayList;

import javax.annotation.Nullable;

public class DisplayFragment extends Fragment {

    EditText searchBar;
    ImageButton loadImages;
    ImageButton verticalButton;
    ImageButton horizontalButton;

    ProgressBar progressBar;

    RecyclerView imageRecycler;
    GetRequestModel getRequestModel;

    ImageAdapter imageAdapter;
    ArrayList<Bitmap> downloadedImages;
    StorageReference storageRef;

    UploadImageModel uploadImageModel;

    GridLayoutManager gridLayoutManager;


    SearchResponseViewModel searchResponseViewModel;
    public DisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRequestModel =new ViewModelProvider(getActivity()).get(GetRequestModel.class);
        searchResponseViewModel =new ViewModelProvider(getActivity()).get(SearchResponseViewModel.class);
        storageRef = FirebaseStorage.getInstance().getReference();
        uploadImageModel = new ViewModelProvider(getActivity()).get(UploadImageModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display, container, false);
        searchBar = view.findViewById(R.id.searchText);
        loadImages = view.findViewById(R.id.searchButton);
        progressBar = view.findViewById(R.id.progressBarId);
        imageRecycler = view.findViewById(R.id.recyclerImages);
        horizontalButton = view.findViewById(R.id.horizontalButton);
        verticalButton = view.findViewById(R.id.verticalButton);

        imageRecycler.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);

        horizontalButton.setVisibility(View.VISIBLE);
        verticalButton.setVisibility(View.VISIBLE);

        //This onclick listener will start the thread for get requesting teh intial request with the search key
        loadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKey = String.valueOf(searchBar.getText());
                if (searchKey != "") {
                    System.out.println("The button is pressed" + searchKey);
                    imageRecycler.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    GetRequestThread searchThread = new GetRequestThread(searchKey, getActivity(), getRequestModel);
                    progressBar.setVisibility(View.VISIBLE);
                    searchThread.start();
                }
            }});


        //When the string response is recieved from the initial get Request this observer then downloads each of the 15
        // images and then uses that to set the recycler view
        getRequestModel.stringResponse.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                System.out.println("Test" + s);
                if (s != "") {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Search Complete", Toast.LENGTH_LONG).show();
                    RetrieveImagesThread retrieveImagesThread = new RetrieveImagesThread(getActivity(), getRequestModel);
                    retrieveImagesThread.start();
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
        });

        horizontalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRequestModel.setDisplaySetting(1);
            }
        });

        verticalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRequestModel.setDisplaySetting(0);
            }
        });

        //When the images are received this observer sets the images on the adapter for hte RecyclerView such that
        //the images populate on the recylerView
        getRequestModel.downloadedImages.observe(getActivity(), new Observer<ArrayList<Bitmap>>() {
            @Override
            public void onChanged(ArrayList<Bitmap> images) {
                if (images.size() > 0) {
                    progressBar.setVisibility(View.GONE);
                    downloadedImages = images;
                    imageRecycler.setVisibility(View.VISIBLE);

                    if (getRequestModel.getDisplaySetting() == 0) {
                        gridLayoutManager = new GridLayoutManager(getActivity(), 1,
                                GridLayoutManager.VERTICAL, false);
                    }
                    else {
                        gridLayoutManager = new GridLayoutManager(getActivity(), 2,
                                GridLayoutManager.VERTICAL, false);
                    }
                    imageRecycler.setLayoutManager(gridLayoutManager);
                    System.out.println("Setting the adapter with images #: " + downloadedImages.size() );
                    imageAdapter = new ImageAdapter(downloadedImages, uploadImageModel, storageRef, getRequestModel);
                    imageRecycler.setAdapter(imageAdapter);
                }
            }});


        //This function simply looks out for changes in the ref variable in the upload viewModel everytime
        // there is a change in the ref it uploads the imageUri using the ref to firebase
        uploadImageModel.ref.observe(getActivity(), new Observer<StorageReference>() {
            @Override
            public void onChanged(StorageReference storageRef) {
                if (storageRef != null && uploadImageModel.getImageURI() != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    storageRef.putFile(uploadImageModel.getImageURI()).addOnSuccessListener(taskSnapshot -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        uploadImageModel.setImageURI(null);
                        uploadImageModel.setRef(null);
                        Toast.makeText(getActivity(), "Image Uploaded Successfully", Toast.LENGTH_LONG).show();
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            System.out.println(downloadUrl);
                        });
                    }).addOnFailureListener(e -> {
                        uploadImageModel.setImageURI(null);
                        uploadImageModel.setRef(null);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Image Uploaded Failed", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    });
                }
            }});
            
        return view;
    }

}
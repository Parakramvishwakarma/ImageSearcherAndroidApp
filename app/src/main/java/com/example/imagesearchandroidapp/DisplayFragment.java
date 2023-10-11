package com.example.imagesearchandroidapp;

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

import java.util.ArrayList;

public class DisplayFragment extends Fragment {

    EditText searchBar;
    ImageButton loadImages;

    ProgressBar progressBar;

    RecyclerView imageRecycler;
    GetRequestModel getRequestModel;

    ImageAdapter imageAdapter;
    ArrayList<Bitmap> downloadedImages;

    SearchResponseViewModel searchResponseViewModel;
    public DisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRequestModel =new ViewModelProvider(getActivity()).get(GetRequestModel.class);
        searchResponseViewModel =new ViewModelProvider(getActivity()).get(SearchResponseViewModel.class);

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

        imageRecycler.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);

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

        getRequestModel.downloadedImages.observe(getActivity(), new Observer<ArrayList<Bitmap>>() {
            @Override
            public void onChanged(ArrayList<Bitmap> images) {
                if (images.size() > 0) {
                    progressBar.setVisibility(View.GONE);
                    downloadedImages = images;
                    imageRecycler.setVisibility(View.VISIBLE);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1,
                            GridLayoutManager.VERTICAL, false);
                    imageRecycler.setLayoutManager(gridLayoutManager);
                    System.out.println("Setting the adapter with images #: " + downloadedImages.size() );
                    imageAdapter = new ImageAdapter(downloadedImages);
                    imageRecycler.setAdapter(imageAdapter);
                }
            }});
        return view;
    }
}
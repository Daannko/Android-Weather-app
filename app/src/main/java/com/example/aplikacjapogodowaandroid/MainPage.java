package com.example.aplikacjapogodowaandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainPage extends Fragment {

    String title;

    public MainPage(String title) {
        this.title = title;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_page, container, false);
        TextView sampleTextView = root.findViewById(R.id.placeInfo);
        sampleTextView.setText(title);
        return root;
    }
}
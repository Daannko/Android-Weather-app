package com.example.aplikacjapogodowaandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ThirdPage extends Fragment {


    String title;

    public ThirdPage(String title) {
        this.title = title;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_third_page, container, false);
        TextView sampleTextView = root.findViewById(R.id.textViewSampleText);
        sampleTextView.setText(this.title);
        return root;
    }
}
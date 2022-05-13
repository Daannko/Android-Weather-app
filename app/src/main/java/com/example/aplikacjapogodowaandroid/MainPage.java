package com.example.aplikacjapogodowaandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainPage extends Fragment {

    String title;
    Weather weather;
    TextView tmp;
    TextView city;
    TextView tmpRange;
    TextView tmpDesc;

    public MainPage(Weather weather) {
        this.weather = weather;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_page, container, false);
        city = root.findViewById(R.id.placeInfo);
        tmp =  root.findViewById(R.id.tempInfo);
        tmpRange =  root.findViewById(R.id.tempFromToInfo);
        tmpDesc =  root.findViewById(R.id.tempDesInfo);

        weather.getWeatherDetail(getView());

        tmp.setText(String.valueOf(weather.tmp.get(0)));

        return root;
    }
}
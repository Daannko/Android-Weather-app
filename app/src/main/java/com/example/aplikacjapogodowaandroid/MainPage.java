package com.example.aplikacjapogodowaandroid;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainPage extends Fragment {

    String title;
    TextView tmp;
    TextView city;
    TextView tmpRange;
    TextView tmpDesc;
    Context mContext;


    String test;

    public MainPage() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_page, container, false);
        city = root.findViewById(R.id.placeInfo);
        tmp =  root.findViewById(R.id.tempInfo);
        tmpRange =  root.findViewById(R.id.tempFromToInfo);
        tmpDesc =  root.findViewById(R.id.tempDesInfo);

        ((MainActivity)mContext).weather.getWeatherDetail(root,new VolleyCallBack() {

        @Override
        public void onSuccess() {
            // this is where you will call the geofire, here you have the response from the volley.
            tmp.setText(String.valueOf(((MainActivity)mContext).weather.tmp.get(0))+"°");
            city.setText(((MainActivity)mContext).weather.cityName);
            tmpRange.setText("Od " + ((MainActivity)mContext).weather.minTmp.get(0)+"° do " + ((MainActivity)mContext).weather.maxTmp.get(0)+"°");
            tmpDesc.setText(((MainActivity)mContext).weather.wDesc.get(0));
        }});

        return root;
    }
}
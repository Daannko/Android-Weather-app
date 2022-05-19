package com.example.aplikacjapogodowaandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SecondPage extends Fragment {

    Context mcontext;
    MainActivity activity;
    ArrayList<String> info ;
    TextView windSpeed;
    TextView windDeg;
    TextView humidity;
    TextView visibility;
    String city;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_second_page, container, false);
        windSpeed = root.findViewById(R.id.WindSpeedInput);
        windDeg = root.findViewById(R.id.WindDegInput);
        humidity = root.findViewById(R.id.HumInput);
        visibility = root.findViewById(R.id.VisibilityInput);

        info = new ArrayList<>();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        city = sharedPref.getString("CityName",null);

        activity = (MainActivity) getActivity();
        if(activity.weather!= null)
        {
            readData(activity.weather);
        }

        return root;
    }

    public void readData(Weather weather)
    {
        if(!weather.windForce.isEmpty())
        {
            info.add(weather.windForce.get(0).toString());
            info.add(weather.windDeg.get(0).toString());
            info.add(weather.humidity.get(0).toString());
            info.add(weather.visibility.get(0).toString());
            updateFragmentInfo();
        }
    }

    public void updateFragmentInfo()
    {
        if(windDeg != null)
        {
            windDeg.setText( info.get(0));
            windSpeed.setText( info.get(1));
            humidity.setText( info.get(2));
            visibility.setText( info.get(3));
        }

    }

}


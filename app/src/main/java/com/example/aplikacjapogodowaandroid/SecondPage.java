package com.example.aplikacjapogodowaandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    ArrayList<String> info;
    TextView windSpeed;
    TextView windDeg;
    TextView humidity;
    TextView visibility;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_second_page, container, false);
        windSpeed = root.findViewById(R.id.WindSpeedInput);
        windDeg = root.findViewById(R.id.WindDegInput);
        humidity = root.findViewById(R.id.HumInput);
        visibility = root.findViewById(R.id.VisibilityInput);

        activity = (MainActivity) getActivity();
        activity.weather.load(getContext(), new VolleyCallBack() {
            @Override
            public void onSuccess() {
                windDeg.setText( info.get(0));
                windSpeed.setText( info.get(1));
                humidity.setText( info.get(2));
                visibility.setText( info.get(3));
            }
        });
        return root;
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


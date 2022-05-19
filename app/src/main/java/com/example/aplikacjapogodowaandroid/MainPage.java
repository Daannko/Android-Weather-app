package com.example.aplikacjapogodowaandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aplikacjapogodowaandroid.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;


public class MainPage extends Fragment {

    private ActivityMainBinding binding;
    String title;
    TextView tmp;
    TextView city;
    TextView tmpRange;
    TextView tmpDesc;
    TextView preasure;
    TextView cord;
    TextView date;
    ImageView iconView;

    Context mContext;

    View root;

    String test;

    public MainPage() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        city = binding.getRoot().findViewById(R.id.placeInfo);
        tmp = binding.getRoot().findViewById(R.id.tempInfo);
        tmpRange =  binding.getRoot().findViewById(R.id.tempFromToInfo);
        tmpDesc =  binding.getRoot().findViewById(R.id.tempDesInfo);
        iconView  = binding.getRoot().findViewById(R.id.icon);
        preasure = binding.getRoot().findViewById(R.id.PreasureLabelInput);
        date= binding.getRoot().findViewById(R.id.DateLabelInput);
        cord= binding.getRoot().findViewById(R.id.CordsLabelInput);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main_page, container, false);
        city = root.findViewById(R.id.placeInfo);
        tmp =  root.findViewById(R.id.tempInfo);
        tmpRange =  root.findViewById(R.id.tempFromToInfo);
        tmpDesc =  root.findViewById(R.id.tempDesInfo);
        iconView  = root.findViewById(R.id.icon);
        preasure = root.findViewById(R.id.PreasureLabelInput);
        date= root.findViewById(R.id.DateLabelInput);
        cord= root.findViewById(R.id.CordsLabelInput);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String cityName = sharedPref.getString("CityName",null);

        if(((MainActivity)mContext).weather.icon != null){
            readData(((MainActivity) mContext).weather);
        }

        return root;
    }

    public void readData(Weather weather)
    {
        if(weather.icon.size() != 0 && cord != null)
        {
            String imageURL = "http://openweathermap.org/img/wn/" + weather.icon.get(0) + "@4x.png";
            Picasso.with(getContext()).load(imageURL).into(iconView);
            cord.setText(String.valueOf(weather.cityCordLat + ", "+weather.cityCordLon));
            date.setText(String.valueOf(weather.date.get(0)));
            preasure.setText(String.valueOf(weather.presure.get(0)+ " hPa"));
            tmp.setText(String.valueOf(weather.tmp.get(0))+"°");
            city.setText(weather.cityName);
            tmpRange.setText("Od " + weather.minTmp.get(0)+"° do " + weather.maxTmp.get(0)+"°");
            tmpDesc.setText(weather.wDesc.get(0));
        }

    }
}
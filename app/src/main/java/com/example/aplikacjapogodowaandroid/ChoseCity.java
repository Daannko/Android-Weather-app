package com.example.aplikacjapogodowaandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChoseCity extends Fragment {

    String selectedCity;
    CityClicked mCallback;

    public ChoseCity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chose_city, container, false);

        ListView cityList = (ListView) root.findViewById(R.id.cityList);
        ArrayList<String> cities = new ArrayList<>();

        cities.add("Warszawa");
        cities.add("Lublin");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,cities);
        cityList.setAdapter(arrayAdapter);

        TextView selectedCityTextView = (TextView) root.findViewById(R.id.selected_city);
        selectedCityTextView.setText("Wybrane miasto: "+selectedCity);


        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           @Override
           public void onItemClick(AdapterView<?> adpterView, View view, int position,
                                   long id) {
               selectedCity = cityList.getItemAtPosition(position).toString();
               selectedCityTextView.setText("Wybrane miasto: "+selectedCity);
               mCallback.updateInfo();

           }
        });

       Button addButton = (Button) root.findViewById(R.id.addButton);
       addButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               TextView cityName = (TextView) root.findViewById(R.id.cityInput);
               if(cityName.getText().toString().length()>2)
               {
                   cities.add(cityName.getText().toString());
                   arrayAdapter.notifyDataSetChanged();
               }
           }
       });
        return root;
    }

    public interface CityClicked{
        public void updateInfo();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (CityClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null; // => avoid leaking, thanks @Deepscorn
        super.onDetach();
    }

}
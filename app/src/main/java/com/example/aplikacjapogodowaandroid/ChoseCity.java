package com.example.aplikacjapogodowaandroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChoseCity extends Fragment {

    ArrayAdapter<String> arrayAdapter;
    ListView cityList;
    View root;
    String selectedCity;
    CityClicked mCallback;
    ArrayList<String> cities = new ArrayList<>();
    SharedPreferences sharedPref;

    public ChoseCity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        root = inflater.inflate(R.layout.fragment_chose_city, container, false);

        Button addButton = (Button) root.findViewById(R.id.addButton);
        TextView selectedCityTextView = (TextView) root.findViewById(R.id.selected_city);
        cityList = (ListView) root.findViewById(R.id.cityList);

        selectedCityTextView.setText("Wybrane miasto: "+selectedCity);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        Gson gson = new Gson();
        String list = sharedPref.getString("CityList",null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        if(list == null)
        {
            cities.add("Lodz");
            SharedPreferences.Editor editor = sharedPref.edit();
            list = gson.toJson(cities);
            editor.putString("CityList",list);
            editor.apply();
        }
        else
        {
            cities = gson.fromJson(list,type);
        }

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,cities);
        cityList.setAdapter(arrayAdapter);

        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           @Override
           public void onItemClick(AdapterView<?> adpterView, View view, int position,
                                   long id) {
               selectedCity = cityList.getItemAtPosition(position).toString();
               selectedCityTextView.setText("Wybrane miasto: "+selectedCity);
               mCallback.updateInfo();

           }
        });

       addButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               TextView cityName = (TextView) root.findViewById(R.id.cityInput);
               if(cityName.getText().toString().length()>2)
               {

                   if(cities.contains(cityName.getText().toString()))
                   {
                       Toast.makeText(getContext(),"This city is already in your list.",Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                       cities.add(cityName.getText().toString());

                       SharedPreferences.Editor editor = sharedPref.edit();
                       String list = gson.toJson(cities);
                       editor.putString("CityList",list);
                       editor.apply();

                       arrayAdapter.notifyDataSetChanged();
                   }




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
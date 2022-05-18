package com.example.aplikacjapogodowaandroid;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikacjapogodowaandroid.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChoseCity extends Fragment {

    private ActivityMainBinding binding;
    ArrayAdapter<String> arrayAdapter;
    ListView cityList;
    View root;
    String selectedCity;
    CityClicked mCallback;
    ArrayList<String> cities = new ArrayList<>();
    SharedPreferences sharedPref;
    TextView selectedCityTextView;
    Context mContext;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public ChoseCity() {
        // Required empty public constructor
    }
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_chose_city, container, false);
        Button addButton = (Button) root.findViewById(R.id.addButton);
        selectedCityTextView = (TextView) root.findViewById(R.id.selected_city);
        cityList = (ListView) root.findViewById(R.id.cityList);

        selectedCityTextView.setText("Wybrane miasto: "+selectedCity);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        Gson gson = new Gson();
        String list = sharedPref.getString("CityList",null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        if(list == null)
        {
            cities.add("Warsaw");
            SharedPreferences.Editor editor = sharedPref.edit();
            list = gson.toJson(cities);
            editor.putString("CityList",list);
            editor.apply();
        }
        else
        {
            cities = gson.fromJson(list,type);
        }

        String cityName = sharedPref.getString("CityName",null);
        if(cityName == null)
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("CityName","Warsaw");
            selectedCity = "Warsaw";
            editor.apply();
        } else selectedCity = cityName;
        selectedCityTextView.setText("Wybrane miasto: "+ selectedCity);


        ////// OBLUGA SWICHA DO TEMPERATURY (C OR F)

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch temp = (Switch) root.findViewById(R.id.switch1);
        String temperature = sharedPref.getString("Temperature",null);

        if(temperature != null)
        {
            if(temperature.equals("F"))
            {
                temp.setChecked(true);
                temp.setText("F");
            }
        }
        temp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(temp.getText().equals("C"))
                {
                    temp.setText("F");
                    Toast.makeText(getContext(),"Wyświetlana temperatura będzie w fahrenheitach",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Temperature","F");
                    editor.apply();
                }
                else{
                    Toast.makeText(getContext(),"Wyświetlana temperatura będzie w celsjuszach",Toast.LENGTH_SHORT).show();
                    temp.setText("C");
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Temperature","C");
                    editor.apply();

                }
                mCallback.updateInfo(selectedCity);
            }
        });

        //////////////// OBLUGA LISY Z MIASTAMI


        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,cities);
        cityList.setAdapter(arrayAdapter);

        /// USTAWIANIA MIASTA
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           @Override
           public void onItemClick(AdapterView<?> adpterView, View view, int position,
                                   long id) {
               setCity(position);
           }


        });


        /// USUWANIE MIASTA
        cityList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                String city = cities.get(position);
                city = StringUtils.stripAccents(city);
                view.getContext().deleteFile(city+".txt");

                cities.remove(position);
                SharedPreferences.Editor editor = sharedPref.edit();
                String list = gson.toJson(cities);
                editor.putString("CityList",list);
                editor.apply();


                arrayAdapter.notifyDataSetChanged();
                mCallback.updateInfo(selectedCity);

                return true;
            }
        });

        Button btn = new Button(((MainActivity)mContext));
        btn.setText("X");
        btn.setTextColor(getResources().getColor(R.color.black));

       addButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               TextView cityName = (TextView) root.findViewById(R.id.cityInput);

               if(cities.contains(cityName.getText().toString()))
               {
                   Toast.makeText(getContext(),"This city is already in your list.",Toast.LENGTH_SHORT).show();
               }
               else
               {
                   ((MainActivity)mContext).weather.askCity(getView(), cityName.getText().toString() ,new VolleyCallBack() {
                       @Override
                       public void onSuccess() {
                           cities.add(cityName.getText().toString());

                           SharedPreferences.Editor editor = sharedPref.edit();
                           String list = gson.toJson(cities);
                           editor.putString("CityList",list);
                           editor.apply();

                           setCity(cities.size()-1);
                           selectedCityTextView.setText("Wybrane miasto: "+selectedCity);
                           arrayAdapter.notifyDataSetChanged();

                           mCallback.updateInfo(selectedCity);
                       }
                   });


               }

           }
       });

        return root;
    }

    public void setCity(int position)
    {
        selectedCity = cityList.getItemAtPosition(position).toString();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("CityName",selectedCity);
        editor.apply();

        selectedCityTextView.setText("Wybrane miasto: "+selectedCity);
        mCallback.updateInfo(selectedCity);
    }

    public interface CityClicked{
        public void updateInfo(String selectedCity);
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
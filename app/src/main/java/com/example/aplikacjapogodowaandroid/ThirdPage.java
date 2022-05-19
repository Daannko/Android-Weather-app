package com.example.aplikacjapogodowaandroid;

import android.app.Activity;
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

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ThirdPage extends Fragment {


    String title;
    MainActivity activity;
    ArrayList<TextView> dateName = new ArrayList<>();
    ArrayList<ImageView> icons = new ArrayList<>();
    ArrayList<TextView> date = new ArrayList<>();
    ArrayList<TextView> tempRange = new ArrayList<>();
    ArrayList<TextView> preasure = new ArrayList<>();
    ArrayList<String> iconsString = new ArrayList<>();
    ArrayList<String> dateString = new ArrayList<>();
    ArrayList<Double> tempRangeMaxString = new ArrayList<>();
    ArrayList<Double> tempRangeMinString = new ArrayList<>();
    ArrayList<Integer> preasureString = new ArrayList<>();
    String city;


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_third_page, container, false);

        TextView temp = root.findViewById(R.id.date_name_1);
        dateName.add(temp);
        temp = root.findViewById(R.id.date_name_2);
        dateName.add(temp);
        temp = root.findViewById(R.id.date_name_3);
        dateName.add(temp);
        temp = root.findViewById(R.id.date_name_4);
        dateName.add(temp);

        ImageView imageView;
        imageView = root.findViewById(R.id.icon_1);
        icons.add(imageView);
        imageView = root.findViewById(R.id.icon_2);
        icons.add(imageView);
        imageView = root.findViewById(R.id.icon_3);
        icons.add(imageView);
        imageView = root.findViewById(R.id.icon_4);
        icons.add(imageView);

        temp = root.findViewById(R.id.date_1);
        date.add(temp);
        temp = root.findViewById(R.id.date_2);
        date.add(temp);
        temp = root.findViewById(R.id.date_3);
        date.add(temp);
        temp = root.findViewById(R.id.date_4);
        date.add(temp);

        temp = root.findViewById(R.id.tempRange_1);
        tempRange.add(temp);
        temp = root.findViewById(R.id.tempRange_2);
        tempRange.add(temp);
        temp = root.findViewById(R.id.tempRange_3);
        tempRange.add(temp);
        temp = root.findViewById(R.id.tempRange_4);
        tempRange.add(temp);

        temp = root.findViewById(R.id.preasure_1);
        preasure.add(temp);
        temp = root.findViewById(R.id.preasure_2);
        preasure.add(temp);
        temp = root.findViewById(R.id.preasure_3);
        preasure.add(temp);
        temp = root.findViewById(R.id.preasure_4);
        preasure.add(temp);



        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        city = sharedPref.getString("CityName",null);

        activity = (MainActivity) getActivity();
        if(activity.weather!= null)
        {
            readData(activity.weather);
        }

        return root;
    }

    public static String dayToDzien(String s)
    {
        switch (s)
        {
            case "Mon":
                return "Poniedziałek";
            case "Tue":
                return "Wtorek";
            case "Wed":
                return "Środa";
            case "Thu":
                return "Czwartek";
            case "Fri":
                return "Piątek";
            case "Sat":
                return "Sobota";
            case "Sun":
                return "Niedziela";

        }
        return s;
    }

    public void readData(Weather weather)
    {
        iconsString = weather.icon;
        tempRangeMaxString = weather.maxTmp;
        tempRangeMinString = weather.minTmp;
        dateString = weather.date;
        preasureString = weather.presure;
        updateInfo();
    }

    public void updateInfo()
    {
        if(dateName != null) {

            for(int i = 0 , j = 1 ; j < tempRangeMaxString.size() && i < dateName.size() ; i++,j++)
            {
                String imageURL = "http://openweathermap.org/img/wn/" + iconsString.get(j) + "@4x.png";
                Picasso.with(getContext()).load(imageURL).into(icons.get(i));

                SimpleDateFormat fullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                Date day = null;
                try {
                    day = fullDate.parse(dateString.get(j).toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat dayDate = new SimpleDateFormat("E");

                String dayString = simpleDateFormat.format(day);
                String dayNameString= dayDate.format(day);

                tempRange.get(i).setText("Od " + tempRangeMinString.get(j)+"° do " + tempRangeMaxString.get(j)+"°");
                dateName.get(i).setText(dayToDzien(dayNameString));
                date.get(i).setText(dayString);
                preasure.get(i).setText(preasureString.get(j).toString()  + "hPa" );
            }
        }
    }
}


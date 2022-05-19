package com.example.aplikacjapogodowaandroid;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Weather {

    private final String url = "http://api.openweathermap.org/data/2.5/forecast?q=";
    private final String appID = "135ed2b36525fa416686f568e19c22cd";

    String citySearch;
    String cityName;
    String tmpSwitch;
    Double cityCordLat,cityCordLon;


    ArrayList<String> icon = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> wDesc = new ArrayList<String>();
    ArrayList<Double> tmp = new ArrayList<Double>();
    ArrayList<Double> maxTmp = new ArrayList<Double>();
    ArrayList<Double> minTmp = new ArrayList<Double>();
    ArrayList<Integer> presure = new ArrayList<>();
    ArrayList<Integer> windForce = new ArrayList<>();
    ArrayList<Integer> windDeg = new ArrayList<>();
    ArrayList<Integer> humidity = new ArrayList<>();
    ArrayList<Integer> visibility = new ArrayList<>();

    SharedPreferences sharedPreferences;

    public static void writeToFile(Context context, String city, String data) {
        try {
            city = StringUtils.stripAccents(city);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(city + ".txt", MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Toast.makeText(context,"Can't save file",Toast.LENGTH_SHORT).show();
        }
    }

    public static String readFromFile(@NonNull Context context, String city) {

        city = StringUtils.stripAccents(city);
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(city + ".txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e);
            return null;
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e);
            return null;
        }

        return ret;
    }

    public void load(Context context, VolleyCallBack volleyCallBack){


        if(citySearch == null)
        {

            sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
            citySearch = sharedPreferences.getString("CityName",null);
            if(citySearch == null)
            {
                citySearch="Warsaw";
            }

        }


        String response = null;
        if(context != null)
        {
            response= readFromFile(context,citySearch);
        }



        if(response == null)
        {
            return;
        }

        wDesc.clear();
        tmp.clear();
        maxTmp.clear();
        minTmp.clear();
        icon.clear();
        date.clear();
        presure.clear();
        windForce.clear();
        windDeg.clear();
        humidity.clear();
        visibility.clear();

        JSONArray list = null;
        JSONObject object = null;
        JSONObject main = null;
        JSONObject hinterval = null;
        JSONArray weather = null;
        JSONObject city = null;
        JSONObject cord = null;
        JSONObject wind = null;

        try {
            object = new JSONObject(response);
            list = object.getJSONArray("list");
            city = object.getJSONObject("city");
            cord = city.getJSONObject("coord");

            cityName = city.getString("name");
            cityCordLat = cord.getDouble("lat");
            cityCordLon = cord.getDouble("lon");




            for(int i = 0 ; i <= 4 ; i++){
                hinterval = list.getJSONObject(i*8);

                main = hinterval.getJSONObject("main");
                weather = hinterval.getJSONArray("weather");
                wind = hinterval.getJSONObject("wind");

                date.add(hinterval.getString("dt_txt"));
                humidity.add(main.getInt("humidity"));

                visibility.add(hinterval.getInt("visibility"));
                windForce.add(wind.getInt("speed"));
                windDeg.add(wind.getInt("deg"));


                presure.add(main.getInt("pressure"));
                tmp.add(convertTmp(main.getDouble("temp"),context));
                maxTmp.add(convertTmp(main.getDouble("temp_max"),context));
                minTmp.add(convertTmp(main.getDouble("temp_min"),context));
                wDesc.add( weather.getJSONObject(0).getString("description"));
                icon.add( weather.getJSONObject(0).getString("icon"));

            }
            volleyCallBack.onSuccess();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void getWeatherDetail(View view,final VolleyCallBack callBack){
        String tempUrl = "";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        if(citySearch == null)
        {
            sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(view.getContext());
            citySearch = sharedPreferences.getString("CityName",null);
            if(citySearch == null)
            {
                citySearch="Warsaw";
            }

        }

        tempUrl = url + citySearch.toLowerCase() + "&appid="+ appID ;
        System.out.println(tempUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                writeToFile(view.getContext(), citySearch,response);
                callBack.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(),"Can't find city.",Toast.LENGTH_SHORT).show();
                Log.d("error",error.toString());
            }
        });

        requestQueue.add(stringRequest);
    }

    public Double convertTmp(Double tmp, Context context)
    {
        int a = 0;
        Double output ;

        if(tmpSwitch == null)
        {
            sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
            tmpSwitch = sharedPreferences.getString("Temperature",null);
            if(tmpSwitch == null)
            {
                tmpSwitch="C";
            }
        }

        switch (tmpSwitch)
        {
            case "C"://CEL
                output =  tmp - 273.15;
                // zaaokrąglenie do 2 liczb po przecinku
                output = Math.floor(output * 10) / 10;
                break;
            case "F"://FARENHEIT
                output = (tmp - 273.15)*9/5 + 32;
                output = Math.floor(output * 10) / 10;
                break;
            default://KELVIN
                output = tmp;
                output = Math.floor(output * 10) / 10;
                break;
        }
        return output;
    }

    public  void askCity(View view,String cityName,final VolleyCallBack callBack){
        String tempUrl = "";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

        tempUrl = url + cityName.toLowerCase() + "&appid="+ appID ;
        System.out.println(tempUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(),"Can't find city.",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

}

package com.example.aplikacjapogodowaandroid;

import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Weather {

    private final String url = "http://api.openweathermap.org/data/2.5/forecast?q=";
    private final String appID = "135ed2b36525fa416686f568e19c22cd";

    String city;
    ArrayList<String> wDesc;
    ArrayList<Double> tmp;






    public  void getWeatherDetail(View view){
        String tempUrl = "";
        String city ="warsaw";
        if (city.equals("")) {
            System.out.println("Puste miasto");
        }
        else
        {
            tempUrl = url + city.toLowerCase() + "&appid="+ appID ;
            System.out.println(tempUrl);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray list = null;
                JSONObject object = null;
                JSONObject main = null;
                JSONObject hinterval = null;
                JSONObject weather = null;



                try {
                    object = new JSONObject(response);
                    list = object.getJSONArray("list");
                    for(int i = 0 ; i <= 3 ; i++){
                        hinterval = list.getJSONObject(i*8);
                        main = hinterval.getJSONObject("main");
                        weather = hinterval.getJSONObject("weather");
                        Double dod = main.getDouble("temp");
                        String desc = weather.getString("description");
                        tmp.add(dod);
                        wDesc.add(desc);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
                Toast.makeText(view.getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
                Log.d("error",error.toString());
                System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);

    }

}

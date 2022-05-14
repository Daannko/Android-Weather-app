package com.example.aplikacjapogodowaandroid;

import static java.lang.Math.round;

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

    String citySearch;
    String cityName;
    Double cityCordLat,cityCordLon;
    ArrayList<String> wDesc = new ArrayList<String>();
    ArrayList<Double> tmp = new ArrayList<Double>();
    ArrayList<Double> maxTmp = new ArrayList<Double>();
    ArrayList<Double> minTmp = new ArrayList<Double>();





    public  void getWeatherDetail(View view,final VolleyCallBack callBack){
        String tempUrl = "";

        wDesc.clear();
        tmp.clear();
        maxTmp.clear();
        minTmp.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        tempUrl = url + citySearch.toLowerCase() + "&appid="+ appID ;
        System.out.println(tempUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray list = null;
                JSONObject object = null;
                JSONObject main = null;
                JSONObject hinterval = null;
                JSONArray weather = null;
                JSONObject city = null;
                JSONObject cord = null;


                try {
                    object = new JSONObject(response);
                    list = object.getJSONArray("list");
                    city = object.getJSONObject("city");
                    cord = city.getJSONObject("coord");

                    cityName = city.getString("name");
                    cityCordLat = cord.getDouble("lat");
                    cityCordLon = cord.getDouble("lat");


                    for(int i = 0 ; i <= 3 ; i++){
                        hinterval = list.getJSONObject(i*8);
                        main = hinterval.getJSONObject("main");
                        weather = hinterval.getJSONArray("weather");

                        tmp.add(convertTmp(main.getDouble("temp")));
                        maxTmp.add(convertTmp(main.getDouble("temp_max")));
                        minTmp.add(convertTmp(main.getDouble("temp_min")));
                        wDesc.add( weather.getJSONObject(0).getString("description"));

                    }
                    callBack.onSuccess();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
                Log.d("error",error.toString());
            }
        });

        requestQueue.add(stringRequest);
    }

    public Double convertTmp(Double tmp)
    {
        int a = 0;
        Double output ;
        switch (a)
        {
            case 0://CEL
                output =  tmp - 273.15;
                // zaaokrąglenie do 2 liczb po przecinku
                output = Math.floor(output * 10) / 10;
                break;
            case 1://FARENHEIT
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

}

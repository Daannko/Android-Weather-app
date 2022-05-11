package com.example.aplikacjapogodowaandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aplikacjapogodowaandroid.databinding.ActivityMainBinding;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String url = "api.openweathermap.org/data/2.5/forecast";
    private String appID = "135ed2b36525fa416686f568e19c22cd";
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewPager2 viewPager2 = findViewById(R.id.ViewPager);

        viewPager2.setAdapter(
                new SampleAdapter(this)
        );
        viewPager2.setCurrentItem(1);
    }

    public void GetWeatherDetail(View view){
        String tempUrl = "";
        String city ="Warszawa";
        if (city.equals("")) {
            System.out.println("Puste miasto");
        }
        else
        {
            tempUrl = url + "?q=" + city + "&appid"+ appID;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    class SampleAdapter extends FragmentStateAdapter
    {

        public SampleAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public SampleAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        public SampleAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position)
            {
                case 0:
                    return new ChoseCity();
                case 1:
                    return new MainPage("główna strona");
                case 2:
                    return new SecondPage("druga strona");
                case 3:
                    return new ThirdPage("trzecia strona");
            }
            return new MainPage("ERROR");
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
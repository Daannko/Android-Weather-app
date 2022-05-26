package com.example.aplikacjapogodowaandroid;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.aplikacjapogodowaandroid.databinding.ActivityMainBinding;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ChoseCity.CityClicked {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    Weather weather = new Weather();
    Integer position;
    ArrayList<Fragment> fragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragments.add(new MainPage());
        fragments.add(new SecondPage());
        fragments.add(new ThirdPage());
        fragments.add(new ChoseCity());



        ViewPager2 viewPager2 = findViewById(R.id.ViewPager);
        viewPager2.setAdapter(
                new SampleAdapter(this)
        );

        weather.getWeatherDetail(binding.getRoot().getRootView(), new VolleyCallBack() {
            @Override
            public void onSuccess() {

            }
        });
        weather.load(binding.getRoot().getContext(), new VolleyCallBack() {
            @Override
            public void onSuccess() {

            }
        });

        Button refreshButton = (Button) binding.getRoot().findViewById(R.id.button2);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo(((ChoseCity)fragments.get(3)).selectedCity);
            }
        });

        Button addCityButton = (Button) binding.getRoot().findViewById(R.id.add_city_button);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewPager2.getVisibility() == View.GONE) {
                    refreshButton.setVisibility(View.VISIBLE);
                    addCityButton.setText(getString(R.string.add_city_button));
                    addCityButton.setTextSize(20);
                    viewPager2.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().remove(fragments.get(3)).commit();
                } else {
                    refreshButton.setVisibility(View.GONE);
                    addCityButton.setText(getString(R.string.add_city_buttonv2));
                    addCityButton.setTextSize(80);
                    viewPager2.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.Frame, fragments.get(3)).commit();
                }
            }
        });


    }





    // ViewPager2
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
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    @Override
    public void updateInfo(String selectedCity) {

        weather.citySearch = selectedCity;
        weather.getWeatherDetail(binding.getRoot().getRootView(),new VolleyCallBack() {
            @Override
            public void onSuccess() {
                weather.load(binding.getRoot().getContext(), new VolleyCallBack() {
                    @Override
                    public void onSuccess() {

                        MainPage tmp  = (MainPage)fragments.get(0);
                        tmp.readData(weather);

                        ArrayList<String> info= new ArrayList<>();
                        info.add(weather.windForce.get(0).toString());
                        info.add(weather.windDeg.get(0).toString());
                        info.add(weather.humidity.get(0).toString());
                        info.add(weather.visibility.get(0).toString());
                        ((SecondPage)fragments.get(1)).info = info;
                        ((SecondPage)fragments.get(1)).updateFragmentInfo();

                        ((ThirdPage)fragments.get(2)).iconsString = weather.icon;
                        ((ThirdPage)fragments.get(2)).tempRangeMaxString = weather.maxTmp;
                        ((ThirdPage)fragments.get(2)).tempRangeMinString = weather.minTmp;
                        ((ThirdPage)fragments.get(2)).dateString = weather.date;
                        ((ThirdPage)fragments.get(2)).preasureString = weather.presure;
                        ((ThirdPage)fragments.get(2)).updateInfo();

                    }
                });
            }});
    }
}
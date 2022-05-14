package com.example.aplikacjapogodowaandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.aplikacjapogodowaandroid.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ChoseCity.CityClicked {

    private ActivityMainBinding binding;
    Weather weather = new Weather();
    ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragments.add(new ChoseCity());
        fragments.add(new MainPage());
        fragments.add(new SecondPage("druga strona"));
        fragments.add(new ThirdPage("trzecia strona"));

        ViewPager2 viewPager2 = findViewById(R.id.ViewPager);
        viewPager2.setAdapter(
                new SampleAdapter(this)
        );
        viewPager2.setCurrentItem(1);
        weather.citySearch = "Warszawa";
        weather.getWeatherDetail(binding.getRoot(),new VolleyCallBack() {
            @Override
            public void onSuccess() {}});
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
            return 4;
        }
    }

    @Override
    public void updateInfo() {

        ChoseCity choseCity = (ChoseCity)fragments.get(0);
        weather.citySearch = choseCity.selectedCity;
        weather.getWeatherDetail(binding.getRoot().getRootView(),new VolleyCallBack() {
            @Override
            public void onSuccess() {
                MainPage tmp  = (MainPage)fragments.get(1);
                tmp.updateFragmentInfo();
            }});



    }

}
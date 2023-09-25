package com.alisoftwares.boom.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.alisoftwares.boom.Adapters.ViewpagerFragmentSetter_MainActivty;
import com.alisoftwares.boom.R;
import com.alisoftwares.boom.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    ViewpagerFragmentSetter_MainActivty viewpagerFragmentSetter_mainActivty;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int color = getResources().getColor(R.color.white);
        getWindow().setNavigationBarColor(color);



        binding.bottomnavigationMenuMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.pollsBottom:
                        binding.ViewPagerMain.setCurrentItem(0);
                        return true;

                    case R.id.repliesBottom:
                        binding.ViewPagerMain.setCurrentItem(1);
                        return true;

                    case R.id.findBottom:
                        binding.ViewPagerMain.setCurrentItem(2);
                        return true;

                    case R.id.settings:
                        binding.ViewPagerMain.setCurrentItem(3);
                        return true;

                    default:
                        return false;
                }
            }
        });
    binding.ViewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 0)
                    binding.bottomnavigationMenuMain.setSelectedItemId(R.id.pollsBottom);
                else if(position==1)
                    binding.bottomnavigationMenuMain.setSelectedItemId(R.id.repliesBottom);
                else if(position==2)
                    binding.bottomnavigationMenuMain.setSelectedItemId(R.id.findBottom);
                else if(position==3)
                    binding.bottomnavigationMenuMain.setSelectedItemId(R.id.settings);



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        viewpagerFragmentSetter_mainActivty =  new ViewpagerFragmentSetter_MainActivty(getSupportFragmentManager());
        binding.ViewPagerMain.setAdapter(viewpagerFragmentSetter_mainActivty);



    }




}
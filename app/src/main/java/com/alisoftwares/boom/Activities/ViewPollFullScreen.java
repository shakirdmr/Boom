package com.alisoftwares.boom.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alisoftwares.boom.R;
import com.alisoftwares.boom.databinding.ActivityViewPollFullScreenBinding;

public class ViewPollFullScreen extends AppCompatActivity {

    ActivityViewPollFullScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPollFullScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int color = getResources().getColor(R.color.white);
        getWindow().setNavigationBarColor(color);

        /*

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }*/


        binding.progressBar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.progressBar1.setProgress(80);
            }
        });



    }
}
package com.alisoftwares.boom.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.alisoftwares.boom.databinding.ActivityPhoneVerifyOtpBinding;

public class PhoneVerify_Activity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    ActivityPhoneVerifyOtpBinding activityPhoneVerifyOtpBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPhoneVerifyOtpBinding = ActivityPhoneVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(activityPhoneVerifyOtpBinding.getRoot());

        sharedPreferences = getSharedPreferences("BoomAppSharedPref", Context.MODE_PRIVATE);

        String uid = sharedPreferences.getString("uid", null);  // Example: Retrieving a String value
        // Example: Retrieving an Integer value
        if(uid !=null) {
            //GOTO MAIN ACTIVITY IF USER ALREADY LOGGED IN
            startActivity(new Intent(PhoneVerify_Activity.this, MainActivity.class));
            finish();
        }

        activityPhoneVerifyOtpBinding.phoneNumber.requestFocus();

        activityPhoneVerifyOtpBinding.continueToOTPVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(activityPhoneVerifyOtpBinding.phoneNumber.getText().toString().isEmpty()) {
                    activityPhoneVerifyOtpBinding.phoneNumber.setError("Enter phone number");
                    return;
                }

                Intent intent = new Intent(PhoneVerify_Activity.this, OTPverify.class);
                intent.putExtra("phoneNumber",activityPhoneVerifyOtpBinding.phoneNumber.getText().toString());
                startActivity(intent);
            }
        });

    }
}
package com.alisoftwares.boom.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alisoftwares.boom.databinding.ActivityOtpverifyBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPverify extends AppCompatActivity {
    String VerificationId;
    ProgressDialog dialog;
    ActivityOtpverifyBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpverifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.SixDigitOTP.requestFocus();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Sending OTP. Please Wait.");
        dialog.setCancelable(false);
        dialog.show();

        String phoneNumber = "+91 "+getIntent().getStringExtra("phoneNumber");
        firebaseAuth = FirebaseAuth.getInstance();

        binding.givenPhone.setText("Verify "+phoneNumber);

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(OTPverify.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String VerifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(VerifyId, forceResendingToken);

                        dialog.dismiss();
                        VerificationId = VerifyId;
                    }
                }).build();

        PhoneAuthProvider.verifyPhoneNumber(options);  //SEND NEW OTP



        binding.verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = binding.SixDigitOTP.getText().toString();

                if(otp.isEmpty()) {
                    binding.SixDigitOTP.setError("Enter OTP");
                    return;
                }

                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(VerificationId, otp);

                firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(OTPverify.this, SetupProfile.class));
                            finishAffinity();
                        } else {
                            Toast.makeText(OTPverify.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}
package com.alisoftwares.boom.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alisoftwares.boom.Modals.User;
import com.alisoftwares.boom.databinding.ActivitySetupProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetupProfile extends AppCompatActivity {

    ActivitySetupProfileBinding activitySetupProfileBinding;
    SharedPreferences sharedPreferences;

    ProgressDialog dialog;
    Uri selectedImage;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySetupProfileBinding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(activitySetupProfileBinding.getRoot());


        activitySetupProfileBinding.userName.requestFocus();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        sharedPreferences = getSharedPreferences("BoomAppSharedPref", Context.MODE_PRIVATE);

        activitySetupProfileBinding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10001);
            }
        });

        activitySetupProfileBinding.continueToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = activitySetupProfileBinding.userName.getText().toString();

                if(name.isEmpty()) {
                    activitySetupProfileBinding.userName.setError("Please Enter A Name");
                return;
                }

                dialog = new ProgressDialog(SetupProfile.this);
                dialog.setMessage("Uploading Image. Please Wait.");
                dialog.setCancelable(false);
                dialog.show();

                        if(selectedImage!=null){

                            StorageReference storageReference = firebaseStorage.getReference().child("Profiles").child(firebaseAuth.getUid());

                            storageReference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if(task.isSuccessful()){
                                            //Successfulll uploaded image

                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    String imageUrl = uri.toString();
                                                    String uid = firebaseAuth.getUid();
                                                    String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                                                    String name = activitySetupProfileBinding.userName.getText().toString();


                                                    User user  = new User(uid,name,phone,imageUrl);

                                                    firebaseDatabase.getReference().child("users")
                                                            .child(uid)
                                                            .setValue(user)
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {

                                                                    Toast.makeText(SetupProfile.this, "Error Occured. Please Try Again Later; "+e.getMessage(), Toast.LENGTH_SHORT).show();

                                                                }
                                                            })
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {

                                                                    dialog.dismiss();

                                                                    makeASharedPrefForLoggedUser(uid,phone);

                                                                    startActivity(new Intent(SetupProfile.this,MainActivity.class));
                                                                    finishAffinity();
                                                                }
                                                            });
                                                }
                                            });
                                        }
                                }
                            });
                        }
                        else
                        {

                        }

            }
        });
    }

    private void makeASharedPrefForLoggedUser(String uid, String phone) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", uid);  // Example: Storing a String value
        editor.putString("phone", phone);  // Example: Storing a String value

        editor.apply();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    if(data!=null)
        if(data.getData()!=null && requestCode==10001){

            selectedImage = data.getData();
            activitySetupProfileBinding.profileImage.setImageURI(data.getData());
        }

    }
}



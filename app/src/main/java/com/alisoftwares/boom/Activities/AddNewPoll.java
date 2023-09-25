package com.alisoftwares.boom.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alisoftwares.boom.Modals.Poll;
import com.alisoftwares.boom.R;
import com.alisoftwares.boom.Modals.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class AddNewPoll extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    EditText et1, et2, et3, et4;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    ImageView imageView_addfeed, imageView_showAddedImage,closeActivity;
    private TextWatcher firstTextWatcher;
    private TextWatcher secondTextWatcher;
    Uri selectedImage;
    public Button sendMyPoll;
    EditText titleOFPoll;
    SharedPreferences sharedPreferences;

    String op1, op2, op3, op4, titleOfThePoll, emailOfCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_poll);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.creamWhite));
        }

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        closeActivity = findViewById(R.id.closeActivity);
        imageView_addfeed = findViewById(R.id.imageView_addfeed);
        imageView_showAddedImage = findViewById(R.id.imageView_showAddedImage);
        sendMyPoll = findViewById(R.id.sendMyPoll);
        titleOFPoll = findViewById(R.id.titleOFPoll);

        titleOFPoll.requestFocus();

        firstTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Set the visibility of the second EditText based on the text in the first EditText
                if (s.length() > 0) {
                    et3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        };
        secondTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Set the visibility of the third EditText based on the text in the second EditText
                if (s.length() > 0) {
                    et4.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        };

        et2.addTextChangedListener(firstTextWatcher);
        et3.addTextChangedListener(secondTextWatcher);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        sendMyPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (titleOFPoll.getText().toString().isEmpty()) {
                    titleOFPoll.requestFocus();
                    titleOFPoll.setError("Enter title");
                    
                } else titleOfThePoll = titleOFPoll.getText().toString();

                if (et1.getText().toString().isEmpty()) {
                    et1.requestFocus();
                    et1.setError("option is required");
                } else op1 = et1.getText().toString();

                if (et2.getText().toString().isEmpty()) {
                    et2.requestFocus();
                    et2.setError("option is required");
                    return;
                } else op2 = et2.getText().toString();

                if (!et3.getText().toString().isEmpty())
                    op3 = et3.getText().toString();
                else op3="null";

                if (!et4.getText().toString().isEmpty())
                    op4 = et4.getText().toString();
                else  op4="null";

                String uid = firebaseAuth.getUid();
                String phone = firebaseAuth.getCurrentUser().getPhoneNumber();

                Date date = new Date();
                String currentTime = String.valueOf(date.getTime());
                String UniqueIdOfPoll = firebaseAuth.getUid()+currentTime;
                
                //NOW UPLOAD TO SERVER AND GOTO HOME
                if(selectedImage!=null){

                    StorageReference storageReference = firebaseStorage.getReference().child("polls").child(firebaseAuth.getUid()).child(UniqueIdOfPoll);

                    storageReference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                //Successfulll uploaded image

                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();


                                        Poll poll  = new Poll(currentTime,uid,phone,imageUrl,titleOfThePoll,op1,op2,op3,op4);

                                        firebaseDatabase.getReference().child("polls")
                                                .child(uid)
                                                .child(UniqueIdOfPoll)
                                                .setValue(poll)
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                       Log.d("SHAKIR", "Error Occured. Please Try Again Later; "+e.getMessage());

                                                    }
                                                })
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {


                                                        startActivity(new Intent(AddNewPoll.this,MainActivity.class));
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
                    Poll poll  = new Poll(currentTime,uid,phone,"null",titleOfThePoll,op1,op2,op3,op4);

                    firebaseDatabase.getReference().child("polls")
                            .child(uid)
                            .child(UniqueIdOfPoll)
                            .setValue(poll)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Log.d("SHAKIR", "Error Occured. Please Try Again Later; "+e.getMessage());

                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


                                    startActivity(new Intent(AddNewPoll.this,MainActivity.class));
                                    finishAffinity();
                                }
                            });
                }

                sharedPreferences = getSharedPreferences("BoomAppSharedPref", MODE_PRIVATE);
                emailOfCurrentUser = sharedPreferences.getString("uid", null);

              // createNewPoll();

            }


        });

        imageView_addfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               openGallery();

            }
        });

        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

    }


    private void cameraOrGallery() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Select Image Source");
        builder.setItems(new CharSequence[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openCamera();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });
        builder.show();

    }

    // Method to open the gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    // Method to open the camera
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Photo captured from the camera
                // Handle the captured image
                // data.getExtras().get("data") contains the image data

                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imageView_showAddedImage.setVisibility(View.VISIBLE);
                imageView_showAddedImage.setImageBitmap(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                // Photo selected from the gallery
                // Handle the selected image
                // data.getData() contains the URI of the selected image

                if (data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();

                    // Use the selected image URI as needed
                    selectedImage  = selectedImageUri;
                    imageView_showAddedImage.setVisibility(View.VISIBLE);
                    imageView_showAddedImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

}
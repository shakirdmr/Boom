    package com.alisoftwares.boom.Fragments;

    import android.net.Uri;
    import android.os.Bundle;

    import androidx.annotation.NonNull;
    import androidx.fragment.app.Fragment;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

    import com.alisoftwares.boom.R;
    import com.alisoftwares.boom.databinding.FragmentSettingsBinding;
    import com.bumptech.glide.Glide;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;


    public class settings extends Fragment {

        FirebaseDatabase firebaseDatabase;
        FirebaseAuth firebaseAuth;
        FragmentSettingsBinding binding;
        String uid;
        public settings() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            binding = FragmentSettingsBinding.inflate(inflater, container, false);
            View view = binding.getRoot();

            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();

            uid = firebaseAuth.getUid();

            firebaseDatabase.getReference().child("users").
                    child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {


                                    String profileImage = dataSnapshot.child("profileImage").getValue(String.class);
                                    String user_name = dataSnapshot.child("name").getValue(String.class);

                                    Glide.with(getContext()).load(profileImage).into(binding.MyprofileImage);
                                    binding.name.setText(user_name);



                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            return  view;
        }
    }
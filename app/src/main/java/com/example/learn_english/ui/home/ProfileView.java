package com.example.learn_english.ui.home;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.learn_english.R;
import com.example.learn_english.model.FireBaseModel;
import com.example.learn_english.model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ProfileView extends Fragment {

    Activity activity;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private TextView profileName, profileEmail,Today;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this.getActivity();
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = root.findViewById(R.id.tvProfileName);
        profileEmail = root.findViewById(R.id.tvProfileEmail);
        Today = root.findViewById(R.id.TodayText);


        DatabaseReference databaseReference = FireBaseModel.getInstanceOfFireBase().getDatabaseReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                profileName.setText(dataSnapshot.child("userName").getValue().toString());
                profileEmail.setText(dataSnapshot.child("userEmail").getValue().toString());
                Today.setText(String.valueOf(dataSnapshot.child("toDay").getValue().toString()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        return root;
    }
}
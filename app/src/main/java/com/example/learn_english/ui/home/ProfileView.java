package com.example.learn_english.ui.home;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learn_english.R;
import com.example.learn_english.model.FireBaseModel;
import com.example.learn_english.model.FlashCard;
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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                profileName.setText(dataSnapshot.child("userName").getValue().toString());
                profileEmail.setText(dataSnapshot.child("userEmail").getValue().toString());
                Today.setText(String.valueOf(dataSnapshot.child("toDay").getValue().toString()));


//                for (DataSnapshot ds : dataSnapshot.child("FlashCards").getChildren()) {
//                    String indexCard = ds.getKey();
//                    int repeat = Integer.parseInt(dataSnapshot.child("FlashCards").child(indexCard).child("repeat").getValue().toString());
//                    String englishText = dataSnapshot.child("FlashCards").child(indexCard).child("englishText").getValue().toString();
//                    String polishText = dataSnapshot.child("FlashCards").child(indexCard).child("polishText").getValue().toString();
//                    String nameOfImage = dataSnapshot.child("FlashCards").child(indexCard).child("nameOfImage").getValue().toString();
//                    String image = dataSnapshot.child("FlashCards").child(indexCard).child("image").getValue().toString();
//                    UserProfile.getInstance().AddUserFishCard(new FlashCard(indexCard,repeat,englishText,polishText,nameOfImage,image));
//
//                }
//                String  toDay = dataSnapshot.child("toDay").getValue().toString();
//                UserProfile.getInstance().setToDay(Integer.parseInt(toDay));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


//        databaseReference.addValueEventListener(new ValueEventListener() {
//
//
//
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
//                profileName.setText(userProfile.getUserName());
//                profileEmail.setText(userProfile.getUserEmail());
//                Today.setText(String.valueOf(userProfile.getToDay()));
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
//            }
        });

        return root;
    }
}
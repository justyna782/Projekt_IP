package com.example.learn_english.model;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learn_english.RegistrationActivity;
import com.example.learn_english.activity_homeCorrect;
import com.example.learn_english.view.Login;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class FireBaseModel {

    public static boolean isLogin = false;
    private static FireBaseModel instance = null;

    public static FireBaseModel getInstanceOfFireBase() {
        if (instance == null) {
            instance = new FireBaseModel();
        }
        return instance;
    }

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public FirebaseStorage getFirebaseStorageReference() {
        return storage;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    private FireBaseModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }


    public void Registration(String email, String password, String name, Activity activity) {

        email = email.trim();
        password = password.trim();

        String finalEmail = email;
        databaseReference = null;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    sendUserData(finalEmail, name);
                    Toast.makeText(activity, "Rejestracja przebiegła pomyślnie", Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(activity, Login.class));
                } else {
                    Toast.makeText(activity, "Rejestracja nie przebiegła pomyślnie!", Toast.LENGTH_SHORT).show();
                }
            }

        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private int counter = 5;
    public void Login(String userName, String userPassword, AppCompatActivity activity)
    {
        FireBaseModel.getInstanceOfFireBase().getFirebaseAuth().signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(activity, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show();
                    getDataAboutLastProfile();
                    activity.startActivity(new Intent(activity, activity_homeCorrect.class));

                } else {
                    Toast.makeText(activity, "Błąd logowania!", Toast.LENGTH_SHORT).show();
                    counter--;
                    Toast.makeText(activity,"Liczba pozostałych prób: " + counter, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }



    public void DeleteFromStorage(FlashCard flasz) {
        if (databaseReference == null && firebaseAuth.getUid() != null)
            databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile.getInstance().RestCard();
                for (DataSnapshot ds : dataSnapshot.child("FlashCards").getChildren()) {
                    String indexCard = ds.getKey();
                    String nameOfImage = dataSnapshot.child("FlashCards").child(indexCard).child("nameOfImage").getValue().toString();
                    if (nameOfImage == flasz.getNameOfImage()) {
                        dataSnapshot.child("FlashCards").child(indexCard).getRef().removeValue();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

// Create a reference to the file to delete
        StorageReference desertRef = storageRef.child("images/" + flasz.getNameOfImage());

// Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });


    }




    private void sendUserData(String email, String name) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        UserProfile userProfile = UserProfile.getInstance(email, name);
        if (databaseReference == null && firebaseAuth.getUid() != null)
            databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.setValue(userProfile);
    }

    public void getDataAboutLastProfile(Activity activity) {
        if (databaseReference == null && firebaseAuth.getUid() != null)
            databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile.getInstance().setFromDataBase(dataSnapshot.getValue(UserProfile.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getDataAboutLastProfile() {
      //  if (databaseReference == null && firebaseAuth.getUid() != null)
            databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile.getInstance().setFromDataBase(dataSnapshot.getValue(UserProfile.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getDataOfFishCard(final UserProfile userProfile) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile.getInstance().RestCard();
                for (DataSnapshot ds : dataSnapshot.child("FlashCards").getChildren()) {
                    String indexCard = ds.getKey();
                    int repeat = Integer.parseInt(dataSnapshot.child("FlashCards").child(indexCard).child("repeat").getValue().toString());
                    String englishText = dataSnapshot.child("FlashCards").child(indexCard).child("englishText").getValue().toString();
                    String polishText = dataSnapshot.child("FlashCards").child(indexCard).child("polishText").getValue().toString();
                    String nameOfImage = dataSnapshot.child("FlashCards").child(indexCard).child("nameOfImage").getValue().toString();
                    String image = dataSnapshot.child("FlashCards").child(indexCard).child("image").getValue().toString();
                    UserProfile.getInstance().AddUserFishCard(new FlashCard(indexCard, repeat, englishText, polishText, nameOfImage, image));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void UpdateProfile(String email, String name) {
        UserProfile.getInstance().setUserEmail(email);
        UserProfile.getInstance().setUserName(name);
        databaseReference.setValue(UserProfile.getInstance());
    }
//
//
//    String  toDay = dataSnapshot.child("toDay").getValue().toString();
//                UserProfile.getInstance().setToDay(Integer.parseInt(toDay));
//
//    String  time = dataSnapshot.child("time").getValue().toString();
//    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                try {
//        Date date = format.parse(time);
//        Date dateToday  = format.parse(format.format(Calendar.getInstance().getTime()));
//        if(date != dateToday)
//        {
//            dataSnapshot.getRef().child("toDay").setValue(0);
//            dataSnapshot.getRef().child("time").setValue(dateToday);
//        }
//        System.out.println(date);
//    } catch (ParseException e) {
//        e.printStackTrace();
//    }


    public int GetUpdate() {
        final int[] test = {0};
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("toDay").getValue().toString();
                test[0] = Integer.getInteger(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        return test[0];
    }

    public void UpdateToday() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("toDay").getValue().toString();
                int val = (Integer.valueOf(value) + 1);
                dataSnapshot.getRef().child("toDay").setValue(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void UpdateCard() {
        UserProfile p = UserProfile.getInstance();

        databaseReference.child("FlashCards").setValue(p.getUserFlashCards());
        databaseReference.child("toDay").setValue(p.getToDay());
    }

    public void AddFishCardToDataBase(FlashCard flash) {
        UserProfile p = UserProfile.getInstance();
        flash.SetNameInFirebase(UUID.randomUUID().toString());
        databaseReference.child("FlashCards").setValue(p.getUserFlashCards());
        uploadImage(flash);
    }

    public FlashCard GetFishCard(FlashCard flash) {


        int i = flash.getIndexImage();
        String image = databaseReference.child("FlashCards").child("FlashCard" + String.valueOf(i)).setValue("image").toString();
        String englishText = databaseReference.child("FlashCards").child("FlashCard" + String.valueOf(i)).setValue("englishText").toString();
        String polishText = databaseReference.child("FlashCards").child("FlashCard" + String.valueOf(i)).setValue("polishText").toString();
        flash.setQuesitonAndAnswer(englishText, polishText, image);
        return flash;
    }

    protected void DeleteImage(FlashCard flasz) {

        if (Uri.parse(flasz.getImage()) != null) {

            // Code for showing progressDialog while uploading


            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + flasz.getNameOfImage());

            // adding listeners on upload
            // or failure of image
            ref.putFile(Uri.parse(flasz.getImage())).removeOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(
                                UploadTask.TaskSnapshot taskSnapshot) {

                            // Image uploaded successfully
                            // Dismiss dialog

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded

                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());

                                }
                            });
        }
    }


    protected void uploadImage(FlashCard flasz) {

        if (Uri.parse(flasz.getImage()) != null) {

            // Code for showing progressDialog while uploading


            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + flasz.getNameOfImage());

            // adding listeners on upload
            // or failure of image
            ref.putFile(Uri.parse(flasz.getImage()))
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded

                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());

                                }
                            });
        }
    }


}

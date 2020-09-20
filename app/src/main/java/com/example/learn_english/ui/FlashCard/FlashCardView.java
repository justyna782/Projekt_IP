package com.example.learn_english.ui.FlashCard;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.learn_english.R;
import com.example.learn_english.model.FireBaseModel;
import com.example.learn_english.model.FlashCard;
import com.example.learn_english.model.UserProfile;
import com.example.learn_english.ui.Find.FindModelViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class FlashCardView
        extends Fragment {

    private List<FlashCard> buffer;
    private List<Bitmap> bufferView;
    private List<FlashCard> userCard;
    private FindModelViewModel findModelViewModel;

    private Button checkbuton;
    private Button Remove;
    private TextView detectedText, translatorText;
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private EditText text;
    private StringBuilder stringBuilder=new StringBuilder();
    private FloatingActionButton floatingActionButton;
    private String first;
    FlashCard flash;
    private FirebaseLanguageIdentification firebaseLanguageIdentification;
    Activity activity;
    Random rand;
    FlashCard card;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        findModelViewModel =
//                ViewModelProviders.of(this).get(FindModelViewModel.class);
        activity = this.getActivity();


        View root = inflater.inflate(R.layout.fragment_flashcard, container, false);
        text = root.findViewById(R.id.Text);
        imageView = root.findViewById(R.id.image_view);
        checkbuton = root.findViewById(R.id.Check);
        buffer = new ArrayList<FlashCard>();
        bufferView = new ArrayList<Bitmap>();
        userCard = new ArrayList<FlashCard>(UserProfile.getInstance().getUserFlashCards());
        rand = new Random();
        if (userCard.size() > 0) {
            rand = new Random();
            setNextCard();
        }else {
            checkbuton.setText("Najpierw Dodaj fiszki!");
            text.setText("Brak fiszek");
        }
        checkbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userCard = new ArrayList<FlashCard>(UserProfile.getInstance().getUserFlashCards());
                if (checkbuton.getText() == "Następna fiszka") {
                    checkbuton.setText("Sprawdź");

                    if (userCard.size() > 0) {
                        setNextCard();
                    } else {
                        checkbuton.setText("Najpierw Dodaj fiszki!");
                        text.setText("Brak fiszek");
                    }

                } else {
                    if(userCard.size() > 0) {
                        checkbuton.setText("Następna fiszka");
                        UserProfile.getInstance().repatOfCard(card);
                        UserProfile.getInstance().DecreaseLeft();
                        FireBaseModel.getInstanceOfFireBase().UpdateCard();
                        text.setText(card.getPolishText());
                    }else {
                        checkbuton.setText("Najpierw Dodaj fiszki!");
                    }
                }
            }
        });

        Remove = root.findViewById(R.id.Remove);
        Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(card != null)
               {
                   FireBaseModel.getInstanceOfFireBase().DeleteFromStorage(card);
                    FireBaseModel.getInstanceOfFireBase().getDataOfFishCard(UserProfile.getInstance());

                   userCard = new ArrayList<FlashCard>(UserProfile.getInstance().getUserFlashCards());
               }

            }
        });

        return root;
    }

    private void removeFromBuffer() {
        buffer.remove(card);
        bufferView.remove(card);
    }


    private void setNextCard()
    {
        card = userCard.get(rand.nextInt(userCard.size()));
        setTry(card);
        text.setText(card.getEnglishText());
    }


    private void setTry(FlashCard card)
    {


    FirebaseStorage storageReference = FireBaseModel.getInstanceOfFireBase().getFirebaseStorageReference();
        StorageReference st = storageReference.getReferenceFromUrl("gs://english-8da46.appspot.com");
        StorageReference pathReference = st.child("images/"+card.getNameOfImage());
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
             //   Picasso.get().load(uri).into(imageView);
            Picasso.get().load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            bufferView.add(bitmap);

        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };




}

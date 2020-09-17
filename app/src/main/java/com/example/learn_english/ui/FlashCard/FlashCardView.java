package com.example.learn_english.ui.FlashCard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.learn_english.R;
import com.example.learn_english.model.DataBase;
import com.example.learn_english.model.FlashCard;
import com.example.learn_english.ui.AddFlashCard.AddFlashCardView;
import com.example.learn_english.ui.Find.FindModelViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;

public class FlashCardView
        extends Fragment {

    private FindModelViewModel findModelViewModel;

    private Button checkbuton;
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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        findModelViewModel =
//                ViewModelProviders.of(this).get(FindModelViewModel.class);
        activity = this.getActivity();
        View root = inflater.inflate(R.layout.fragment_flashcard, container, false);
        text = root.findViewById(R.id.Text);
        imageView = root.findViewById(R.id.image_view);
        setTry(3);
        checkbuton = root.findViewById(R.id.Check);

        checkbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkbuton.getText() == "Następna fiszka")
                {
                    checkbuton.setText("Sprawdź");
                    setTry(1);
                }
                else {
                    checkbuton.setText("Następna fiszka");
                    text.setText(flash.getPolishText());
                }
            }
        });
//        firebaseAuth = FirebaseAuth.getInstance();
//        detectedText = root.findViewById(R.id.detected_text);
//        imageView = root.findViewById(R.id.image_view);
//        translatorText = root.findViewById(R.id.translator_text);
//        pickImageBtn = root.findViewById(R.id.pick_image_btn);
//        firebaseLanguageIdentification = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
//
//
//        pickImageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stringBuilder.setLength(0);
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 1);
//            }
//        });
        return root;
    }

    private void setTry(int keyid)
    {
        DataBase dataBase = DataBase.getInstance(activity);
       flash= dataBase.getFlashcard(keyid);
        text.setText(flash.getEnglishText());
        imageView.setImageBitmap(BitmapFactory.decodeFile(flash.getImage()));
    }
}

package com.example.learn_english.ui.DetectText;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.learn_english.DetectText;
import com.example.learn_english.R;
import com.example.learn_english.ui.AddFlashCard.AddFlashCardView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

public class DetectTextView extends Fragment {

    private DetectTextViewModel detectTextViewModel;
    private Button pickImageBtn;
    private TextView detectedText, translatorText;
    private FirebaseAuth firebaseAuth;

    private FloatingActionButton floatingActionButton;
    private ImageView imageView;
    private StringBuilder stringBuilder=new StringBuilder();
    private FirebaseLanguageIdentification firebaseLanguageIdentification;
    Activity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detectTextViewModel =
                ViewModelProviders.of(this).get(DetectTextViewModel.class);

        activity = this.getActivity();
        View root = inflater.inflate(R.layout.fragment_detect_text, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        detectedText = root.findViewById(R.id.detected_text);
        imageView = root.findViewById(R.id.image_view);
        translatorText= root.findViewById(R.id.translator_text);
        pickImageBtn = root.findViewById(R.id.pick_image_btn);
        firebaseLanguageIdentification = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        floatingActionButton = root.findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddFlashCardView.class);
                startActivity(i);
            }
        });

        pickImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuilder.setLength(0);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        pickImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuilder.setLength(0);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        return root;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
            {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do something you want
                } else {
                    // permission denied

                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==activity.RESULT_OK)
        {
            if(requestCode==1)
            {
                Uri uri = data.getData();

                if(uri!=null) {
                    String [] filePath={MediaStore.Images.Media.DATA};
                    Cursor cursor = activity.getContentResolver().query(uri, filePath, null, null, null);
                    cursor.moveToFirst();

                    int index=cursor.getColumnIndex(filePath[0]);
                    String finalFilePath=cursor.getString(index);

                    cursor.close();
                    imageView.setImageBitmap(BitmapFactory.decodeFile(finalFilePath));

                    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(BitmapFactory.decodeFile(finalFilePath));
                    FirebaseVisionTextRecognizer dectector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                    dectector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>()
                    {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText)
                        {
                            String text = firebaseVisionText.getText();
                            stringBuilder.append("Detected text: ").append(text).append("\n");
                            detectedText.setText(stringBuilder.toString());
                            translate();
                        }
                    });
                }
            }
        }

    }

    private  void translate() {
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(FirebaseTranslateLanguage.PL)
                        .build();
        final FirebaseTranslator englishGermanTranslator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                // Model downloaded successfully. Okay to start translating.
                                // (Set a flag, unhide the translation UI, etc.)
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                // ...
                            }
                        });

        englishGermanTranslator.translate(stringBuilder.toString())
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                translatorText.setText(translatedText);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                translatorText.setText("błąd");
                            }
                        });

    }


}
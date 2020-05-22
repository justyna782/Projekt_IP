package com.example.learn_english;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class DetectText extends AppCompatActivity {

    private Button pickImageBtn;
    private TextView detectedText, translatorText;
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private StringBuilder stringBuilder=new StringBuilder();

private FirebaseLanguageIdentification firebaseLanguageIdentification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_text);

        firebaseAuth = FirebaseAuth.getInstance();
        detectedText = findViewById(R.id.detected_text);
        imageView = findViewById(R.id.image_view);
        translatorText=findViewById(R.id.translator_text);
        pickImageBtn = findViewById(R.id.pick_image_btn);
        firebaseLanguageIdentification = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        ActivityCompat.requestPermissions(DetectText.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        pickImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuilder.setLength(0);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
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
                    Toast.makeText(DetectText.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode,resultCode,data);
            if (resultCode==RESULT_OK)
            {
                if(requestCode==1)
                {
                    Uri uri = data.getData();

                    if(uri!=null) {
                        String [] filePath={MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(uri, filePath, null, null, null);
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


        @Override
        protected void onStart()
    {
            super.onStart();

            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser==null)
            {
                signInUser();
            }
        }

        private void signInUser()
        {
            Intent intent = new Intent(DetectText.this,LoginActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(intent);
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


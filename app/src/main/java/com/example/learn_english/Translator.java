package com.example.learn_english;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateRemoteModel;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.Set;

public class Translator extends AppCompatActivity {

    private EditText mSourcetext;
    private Button mTranslateBtn;
    private TextView mTranslatedText;
    private String sourceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
        mSourcetext = findViewById(R.id.word_pl);
        mTranslateBtn = findViewById(R.id.btnTranstale);
        mTranslatedText = findViewById(R.id.word_en);


        mTranslateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate();
            }
        });

    }
    private  void translate() {
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.PL)
                        .setTargetLanguage(FirebaseTranslateLanguage.EN)
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
        sourceText = mSourcetext.getText().toString();
        englishGermanTranslator.translate(sourceText)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                mTranslatedText.setText(translatedText);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mTranslatedText.setText("błąd");
                            }
                        });

        FirebaseModelManager modelManager = FirebaseModelManager.getInstance();

// Get translation models stored on the device.
        modelManager.getDownloadedModels(FirebaseTranslateRemoteModel.class)
                .addOnSuccessListener(new OnSuccessListener<Set<FirebaseTranslateRemoteModel>>() {
                    @Override
                    public void onSuccess(Set<FirebaseTranslateRemoteModel> models) {
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });

// Delete  model if it's on the device.
        FirebaseTranslateRemoteModel deModel =
                new FirebaseTranslateRemoteModel.Builder(FirebaseTranslateLanguage.DE).build();
        modelManager.deleteDownloadedModel(deModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model deleted.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });

// Download  model.
        FirebaseTranslateRemoteModel enModel =
                new FirebaseTranslateRemoteModel.Builder(FirebaseTranslateLanguage.EN).build();
        FirebaseModelDownloadConditions conditions2 = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        modelManager.download(enModel, conditions2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });
        FirebaseTranslateRemoteModel plModel =
                new FirebaseTranslateRemoteModel.Builder(FirebaseTranslateLanguage.PL).build();
        FirebaseModelDownloadConditions conditions3 = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        modelManager.download(plModel, conditions3)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });
    }


}


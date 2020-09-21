package com.example.learn_english.ui.translator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.learn_english.R;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class TranslatorView extends Fragment {

    Activity activity;
    private EditText mSourcetext;
    private Button mTranslateBtn, changeLanguages;
    private TextView mTranslatedText;
    private String sourceText;
    private CharSequence lang, word_to_translate, word_after_translation;

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this.getActivity();
        View root = inflater.inflate(R.layout.fragment_translator, container, false);
        mSourcetext = root.findViewById(R.id.word_to_translate);
        mTranslateBtn = root.findViewById(R.id.btnTranstale);
        mTranslatedText = root.findViewById(R.id.word_after_translation);
        changeLanguages = root.findViewById(R.id.change_languages);


        changeLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang = changeLanguages.getText();
                word_after_translation = mTranslatedText.getText();
                word_to_translate = mSourcetext.getText();

                if (lang.equals("PL -> EN")) {
                    changeLanguages.setText("EN -> PL");

                } else {
                    changeLanguages.setText("PL -> EN");
                }
                if (!TextUtils.isEmpty(word_after_translation) && !TextUtils.isEmpty(word_to_translate)) {
                    mTranslatedText.setText(word_to_translate);
                    mSourcetext.setText(word_after_translation);
                }

            }
        });

        mTranslateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang = changeLanguages.getText();
                if (lang.equals("PL -> EN")) {
                    translatePlEn();
                    word_after_translation = mTranslatedText.getText();
                } else {
                    translateEnPl();
                    word_after_translation = mTranslatedText.getText();
                }
            }
        });
        return root;
    }

    private void translatePlEn() {
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

    private void translateEnPl() {
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(FirebaseTranslateLanguage.PL)
                        .build();
        final FirebaseTranslator englishTranslator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        englishTranslator.downloadModelIfNeeded(conditions)
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
        englishTranslator.translate(sourceText)
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
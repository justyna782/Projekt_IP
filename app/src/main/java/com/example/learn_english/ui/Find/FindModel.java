package com.example.learn_english.ui.Find;

import android.app.Activity;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.learn_english.R;
import com.example.learn_english.ui.AddFlashCard.AddFlashCardView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.util.List;

public class FindModel extends Fragment {

    private FindModelViewModel findModelViewModel;

    private Button pickImageBtn;
    private TextView detectedText, translatorText;
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private String finalFilePath;
    private StringBuilder stringBuilder=new StringBuilder();
    private FloatingActionButton floatingActionButton;
    private String first;
    private FirebaseLanguageIdentification firebaseLanguageIdentification;
    Activity activity;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findModelViewModel =
                ViewModelProviders.of(this).get(FindModelViewModel.class);
        activity = this.getActivity();
        View root = inflater.inflate(R.layout.fragment_find_object, container, false);

        floatingActionButton = root.findViewById(R.id.fab);
        firebaseAuth = FirebaseAuth.getInstance();
        detectedText = root.findViewById(R.id.detected_text);
        imageView = root.findViewById(R.id.image_view);
        translatorText=root.findViewById(R.id.translator_text);
        pickImageBtn = root.findViewById(R.id.pick_image_btn);
        firebaseLanguageIdentification = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddFlashCardView.class);
                i.putExtra("image",finalFilePath);
                i.putExtra("text",first);
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
        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==activity.RESULT_OK)
        {
            first = null;
            if(requestCode==1)
            {
                Uri uri = data.getData();

                if(uri!=null) {
                    String [] filePath={MediaStore.Images.Media.DATA};
                    Cursor cursor = activity.getContentResolver().query(uri, filePath, null, null, null);
                    cursor.moveToFirst();

                    int index=cursor.getColumnIndex(filePath[0]);
                    finalFilePath =cursor.getString(index);

                    cursor.close();
                    imageView.setImageBitmap(BitmapFactory.decodeFile(finalFilePath));

                    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(BitmapFactory.decodeFile(finalFilePath));

                    ///!!!!!!!
                    FirebaseVisionImageLabeler dectector = FirebaseVision.getInstance().getOnDeviceImageLabeler();
                    dectector.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>()
                    {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels)
                        {

                            for(FirebaseVisionImageLabel label: firebaseVisionImageLabels)
                            {
                                if(first == null)
                                   first = label.getText();
                                double percent = Math.round(label.getConfidence() * 100) ;
                                stringBuilder.append("Image label: ").append(label.getText()).append("\n").append("Precision: ").append(percent).append("% \n");
                            }

                            detectedText.setText(stringBuilder.toString());






                        }
                    });
                }
            }
        }

    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser==null)
        {

        }
    }



   






}
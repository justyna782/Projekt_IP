package com.example.learn_english;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

public class Labeling extends AppCompatActivity {

    private Button pickImageBtn;
    private TextView detectedText, translatorText;
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private StringBuilder stringBuilder=new StringBuilder();

    private FirebaseLanguageIdentification firebaseLanguageIdentification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labeling);

        firebaseAuth = FirebaseAuth.getInstance();
        detectedText = findViewById(R.id.detected_text);
        imageView = findViewById(R.id.image_view);
        translatorText=findViewById(R.id.translator_text);
        pickImageBtn = findViewById(R.id.pick_image_btn);
        firebaseLanguageIdentification = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        ActivityCompat.requestPermissions(Labeling.this,
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
                    Toast.makeText(Labeling.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
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

                    ///!!!!!!!
                    FirebaseVisionImageLabeler dectector = FirebaseVision.getInstance().getOnDeviceImageLabeler();
                    dectector.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>()
                    {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels)
                        {

                            for(FirebaseVisionImageLabel label: firebaseVisionImageLabels)
                            {
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
        Intent intent = new Intent(Labeling.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

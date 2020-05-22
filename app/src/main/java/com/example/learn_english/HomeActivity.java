package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void goToFind(View view) {
        if(view.getId() == R.id.findId)
        {
            //Do something Like starting an activity
            Intent intent = new Intent(HomeActivity.this, Labeling.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.profile_image)
        {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.translatorId)
        {
            Intent intent = new Intent(HomeActivity.this, Translator.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.detectTextId)
        {
            Intent intent = new Intent(HomeActivity.this, DetectText.class);
            startActivity(intent);
        }
    }
}

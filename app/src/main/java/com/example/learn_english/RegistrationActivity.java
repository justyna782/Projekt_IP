package com.example.learn_english;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learn_english.model.FireBaseModel;
import com.example.learn_english.model.FlashCard;
import com.example.learn_english.model.UserProfile;
import com.example.learn_english.view.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, userPassword, userEmail;

    private Button regButton;
    private TextView userLogin;
    String name, password, email;
    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();



        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                    FireBaseModel.getInstanceOfFireBase().Registration(email,password,name, RegistrationActivity.this);

            }
        });
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, Login.class));
            }
        });



    }

    private void setupUIViews() {
        userName = findViewById(R.id.etUserName);
        userPassword = findViewById(R.id.etUserPassword);
        userEmail = findViewById(R.id.etUserEmail);
        regButton = findViewById(R.id.btnRegister);
        userLogin = findViewById(R.id.tvUserLogin);

    }

    private Boolean validate() {
        Boolean result = false;

        name = userName.getText().toString();
        password = userPassword.getText().toString();
        email = userEmail.getText().toString();


        if (password.length() < 6) {
            Toast.makeText(this, "Hasło musi składać się z co najmniej 6 znaków ", Toast.LENGTH_SHORT).show();
        }
        if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Podaj wszystkie dane!", Toast.LENGTH_SHORT).show();
        } else {

            result = true;
        }

        return result;
    }



}

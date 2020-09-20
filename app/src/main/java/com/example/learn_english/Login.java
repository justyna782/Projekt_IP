package com.example.learn_english;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learn_english.model.FireBaseModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {


    private EditText Name;
    private EditText Password;
    private TextView Info;
    public Button Login;
    public Button getLogin()
    {
        return Login;
    }
    private int counter = 5;
    private TextView userRegistration;
 //   private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = findViewById(R.id.etName);
        Password = findViewById(R.id.etPassword);
        Info = findViewById(R.id.tvInfo);
        Login = findViewById(R.id.btnLogin);
        userRegistration = findViewById(R.id.tvRegister);
        forgotPassword = findViewById(R.id.tvForgotPassword);

        Info.setText("Liczba pozostałych prób: 5");

//        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user =  FireBaseModel.getInstanceOfFireBase().getFirebaseAuth().getCurrentUser();

        if (user != null) {
            finish();
            startActivity(new Intent(com.example.learn_english.Login.this, activity_homeCorrect.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.example.learn_english.Login.this, RegistrationActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.example.learn_english.Login.this, PasswordActivity.class));
            }
        });
    }

    private void validate(String userName, String userPassword) {

  FireBaseModel.getInstanceOfFireBase().Login(userName,userPassword,this);
                    if (counter == 0) {
                        counter--;
                        Login.setEnabled(false);
                    }


    }


}

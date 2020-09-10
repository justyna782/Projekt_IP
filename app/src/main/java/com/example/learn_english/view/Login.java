package com.example.learn_english.view;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learn_english.ui.home.HomeView;
import com.example.learn_english.ui.home.HomeViewModel;
import com.example.learn_english.PasswordActivity;
import com.example.learn_english.R;
import com.example.learn_english.RegistrationActivity;
import com.example.learn_english.activity_homeCorrect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;


    String name = "elo";
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

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            finish();
            startActivity(new Intent(com.example.learn_english.view.Login.this, activity_homeCorrect.class));
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
                startActivity(new Intent(com.example.learn_english.view.Login.this, RegistrationActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.example.learn_english.view.Login.this, PasswordActivity.class));
            }
        });
    }

    private void validate(String userName, String userPassword) {

        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(com.example.learn_english.view.Login.this, HomeView.class));

                } else {
                    Toast.makeText(com.example.learn_english.view.Login.this, "Błąd logowania!", Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("Liczba pozostałych prób: " + counter);
                    progressDialog.dismiss();
                    if (counter == 0) {
                        Login.setEnabled(false);
                    }
                }
            }
        });


    }


    public void onButtonClick(View view) {
        ImageButton button = (ImageButton)view;
        button.setSelected(!button.isSelected());
        EditText editText = findViewById(R.id.etPassword);

        if (button.isSelected()) {
            button.setImageResource(R.drawable.invisibility_password);
            Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            //Handle selected state change
        } else {
            button.setImageResource(R.drawable.visibility_password);
            Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            //Handle de-select state change
        }
    }
}

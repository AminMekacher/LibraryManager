package com.usermanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.usermanager_demo.R;

public class LoginActivity extends AppCompatActivity {

    private EditText emailText, passwordText;
    private Button loginButton;
    private String emailHolder, passwordHolder;
    private TextView registerText, forgottenPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserCredentials();
            }
        });

        registerText = findViewById(R.id.registerText);
        forgottenPassword = findViewById(R.id.passwordForgotten);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void checkUserCredentials() {

        emailHolder = emailText.getText().toString();
        passwordHolder = passwordText.getText().toString();

        Log.e("Login", "Login for " + emailHolder + "with password " + passwordHolder);

        mAuth.signInWithEmailAndPassword(emailHolder, passwordHolder)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("Login", "Function called");
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Worked!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Didnt work!", Toast.LENGTH_LONG).show();
                        }

                        if (!task.isSuccessful()) {
                            Log.e("Login", "Big unknown error");
                        }
                    }
                });

    }

    public void registerCallback(View view) {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void passwordRecover(View view) {

        Intent intent = new Intent(getApplicationContext(), ForgottenPassword.class);
        startActivity(intent);
    }
}

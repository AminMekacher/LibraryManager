package com.usermanager;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.usermanager_demo.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailText, passwordText, usernameText, pictureText;
    private Button registerButton, cancelButton;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    // Boolean to check if all entries in the form have been filled
    private Boolean registerFormStatus;
    String usernameHolder, passwordHolder, emailHolder, pictureHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        emailText = findViewById(R.id.email);
        pictureText = findViewById(R.id.profilePicture);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference().child("Users");

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFormStatus();

                if (registerFormStatus) {
                    addProfileToFirebase();
                }
            }
        });

    }

    private void addProfileToFirebase() {

        firebaseAuth.createUserWithEmailAndPassword(emailHolder, passwordHolder)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {

                        if(task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Worked!", Toast.LENGTH_LONG).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(usernameHolder)
                                    .setPhotoUri(Uri.parse(pictureHolder))
                                    .build();


                            user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.e("Picture", "Everything went well :)");
                                            addUserToDatabase();
                                        }
                                    }
                                });

                        } else {
                            Log.d("FirebaseAuth", "onComplete" + task.getException().getMessage());
                            Toast.makeText(RegisterActivity.this, "Didnt work", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void addUserToDatabase() {

        myRef.child(usernameHolder).child("username").setValue(usernameHolder);
        myRef.child(usernameHolder).child("picture").setValue(pictureHolder);

        completeRegistration();

    }

    private void completeRegistration() {
        Intent intent = new Intent(RegisterActivity.this, LottieActivity.class);
        startActivity(intent);
    }

    public void checkFormStatus() {

        usernameHolder = usernameText.getText().toString().trim();
        passwordHolder = passwordText.getText().toString().trim();
        emailHolder = emailText.getText().toString().trim();
        pictureHolder = pictureText.getText().toString().trim();

        if (TextUtils.isEmpty(usernameHolder) || TextUtils.isEmpty(passwordHolder) ||
                TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(pictureHolder)) {
            registerFormStatus = false;
        } else {
            registerFormStatus = true;
        }

    }
}

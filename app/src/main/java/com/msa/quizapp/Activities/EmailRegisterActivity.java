package com.msa.quizapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.msa.quizapp.R;

public class EmailRegisterActivity extends AppCompatActivity {
    Button register,login;
    EditText name, email, password;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        name = findViewById(R.id.nameRegistration);
        email = findViewById(R.id.emailRegistration);
        password = findViewById(R.id.passwordRegistration);

        login = findViewById(R.id.want_to_login);
        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailRegisterActivity.this, LoginActivity.class));
            }
        });


        register = findViewById(R.id.emailRegisterButton);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String getName = name.getText().toString().trim();
                String getEmail = email.getText().toString().trim();
                String getPassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(getEmail)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(getPassword)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (getPassword.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                }

                //create user
                auth.createUserWithEmailAndPassword(getEmail, getPassword)
                        .addOnCompleteListener(EmailRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(EmailRegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(EmailRegisterActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(EmailRegisterActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });

            };


        });
    }
}

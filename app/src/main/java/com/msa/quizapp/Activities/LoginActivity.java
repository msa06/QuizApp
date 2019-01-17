package com.msa.quizapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msa.quizapp.Model.Quiz;
import com.msa.quizapp.Model.User;
import com.msa.quizapp.R;

public class LoginActivity extends AppCompatActivity {
    Toolbar mToolbar;
    TextView mToolTitle;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    //Firebase Realtime Database
    private DatabaseReference mUserDatabaseReference;
    private ValueEventListener mValueEventListner;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private final static int RC_SIGN_IN = 2;
    private final static String TAG = "LOGIN ACTIVITY";
    private TextView email_register;
    Button email_signin;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        email_register = (TextView) findViewById(R.id.registertext);
        email_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, EmailRegisterActivity.class));

            }
        });

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        email_signin = (Button) findViewById(R.id.login_btn);

        email_signin.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String emailString = email.getText().toString();
                                                final String passwordString = password.getText().toString();

                                                if (TextUtils.isEmpty(emailString)) {
                                                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                if (TextUtils.isEmpty(passwordString)) {
                                                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }


                                                //authenticate user
                                                mAuth.signInWithEmailAndPassword(emailString, passwordString)
                                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                // If sign in fails, display a message to the user. If sign in succeeds
                                                                // the auth state listener will be notified and logic to handle the
                                                                // signed in user can be handled in the listener.
                                                                //progressBar.setVisibility(View.GONE);
                                                                if (!task.isSuccessful()) {
                                                                    // there was an error
                                                                    if (password.length() < 6) {
                                                                        password.setError(getString(R.string.minimum_password));
                                                                    } else {
                                                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                                                    }
                                                                } else {
                                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                            }
                                        });


        //Setting Signin Button
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        //Firebase Google Sign in
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            mUser = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI();
                        }

                        // ...
                    }
                });
    }

    private void updateUI() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void saveUserData(FirebaseUser user) {
        String uid = user.getUid();
        String name = user.getDisplayName();
        String email = user.getEmail();
        User userdata = new User(uid, name, email, 0, 0);
        mUserDatabaseReference.child(uid).setValue(userdata);
    }


    @Override
    public void onStart() {
        super.onStart();
       FirebaseUser mUser = mAuth.getCurrentUser();
       if(mUser!=null){
           updateUI();
           attachDatabaseReadListner();
       }
    }

    private void attachDatabaseReadListner() {
        if (mValueEventListner == null) {
            mValueEventListner = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(mUser.getUid())) {
                        saveUserData(mUser);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mUserDatabaseReference.addValueEventListener(mValueEventListner);
        }
    }
    private void detachedDatabaseReadListner() {
        if (mValueEventListner!=null){
            mUserDatabaseReference.removeEventListener(mValueEventListner);
            mValueEventListner=null;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachedDatabaseReadListner();
    }
}

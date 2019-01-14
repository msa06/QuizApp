package com.msa.quizapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msa.quizapp.Model.Rank;
import com.msa.quizapp.Model.User;
import com.msa.quizapp.R;

public class ResultActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mToolTitle;
    private TextView result_text;
    private TextView result_message;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference currentUserReference;
    private DatabaseReference rankingReference;
    private int result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //Set Toolbar
        //Setting Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolTitle = (TextView) findViewById(R.id.toolbar_title);

        setSupportActionBar(mToolbar);
        mToolTitle.setText(mToolbar.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       //Get Intent;
        Intent resultIntent = getIntent();
        result = resultIntent.getIntExtra("Result",0);

        //Get Reference
        result_text = (TextView) findViewById(R.id.result_text);
        result_message = (TextView) findViewById(R.id.result_message);

        //set Result Text
        result_text.setText(result+"");
        setResultMessage(result);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        rankingReference = FirebaseDatabase.getInstance().getReference().child("Ranking");
        currentUserReference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User currentUser = dataSnapshot.getValue(User.class);
                addTotaltoUser(currentUser);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void addTotaltoUser(User currentUser) {
            int userTotal = currentUser.getTotalpt();
            userTotal += result;
            currentUser.setTotalpt(userTotal);
            currentUserReference.child(currentUser.getUid()).setValue(currentUser);
            Rank rank = new Rank(mUser.getUid(),mUser.getPhotoUrl().toString(),currentUser.getName(),currentUser.getTotalpt());
            rankingReference.child(currentUser.getUid()).setValue(rank);

    }

    private void setResultMessage(int result) {
        String message="";
        switch (result){
            case 0: message = "Duck";
                break;
            case 1: message = "There is Hope";
                break;
            case 2: message = "There is Hope";
                break;
            case 3: message = "There is Hope";
                break;
            case 4: message = "Try for More";
                break;
            case 5: message = "Try for More";
                break;
            case 6: message = "Try for More";
                break;
            case 7: message = "Smart!";
                break;
            case 8: message = "Excellent!";
                break;
            case 9: message = "Close!!";
                break;
            case 10: message = "Perfect's Score!!!";
                break;
        }
        result_message.setText(message);
    }
}

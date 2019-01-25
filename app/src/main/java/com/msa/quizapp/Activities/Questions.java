package com.msa.quizapp.Activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msa.quizapp.Model.Question;
import com.msa.quizapp.Model.User;
import com.msa.quizapp.R;

import java.util.ArrayList;

public class Questions extends AppCompatActivity {
    QuestionView questionView;
    Button button;
    FragmentManager fragman;
    boolean fraginplace;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        questionView=new QuestionView();
        fraginplace=false;

        fragman= getSupportFragmentManager();
        button=(Button)findViewById(R.id.bringques);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfragement(questionView);
            }
        });
    }

    private void setfragement(Fragment frag) {
        fraginplace=true;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.mainframe, frag).commit();
    }
    public void onBackPressed() {

        if(fraginplace)
        {
            if (fragman.getBackStackEntryCount() == 0) {
                fraginplace=false;
                this.finish();
            } else {
                fragman.popBackStack();
            }
        }
    }





}

package com.msa.quizapp.Activities;

import android.content.Intent;
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
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    int counter;
    ArrayList <Question> questions;
    TextView questiionView,op1,op2,op3,op4;
    ProgressBar progressBar;
    Button nextQuestionbtn;
    //Firebase Realtime Database
    private DatabaseReference mUserDatabaseReference;
    private ValueEventListener mValueEventListner;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        counter=1;
        questions = new ArrayList<>();
        questiionView=(TextView)findViewById(R.id.questionView);
        nextQuestionbtn=(Button)findViewById(R.id.nextQuestion);
        op1=(TextView)findViewById(R.id.option1);
        op2=(TextView)findViewById(R.id.option2);
        op3=(TextView)findViewById(R.id.option3);
        op4=(TextView)findViewById(R.id.option4);
        progressBar=(ProgressBar)findViewById(R.id.Questionprogress);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Question");
        progressBar.setVisibility(View.VISIBLE);
        onAuthSuccess(mUser);
        nextQuestionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter < 5) {
                    displayNextQuestion(counter);
                    counter++;
                }
            }
        });
    }

    private void displayNextQuestion(int counter) {
        questiionView.setText(questions.get(counter).getQues());
        op1.setText(questions.get(counter).getOp1());
        op2.setText(questions.get(counter).getOp2());
        op3.setText(questions.get(counter).getOp3());
        op4.setText(questions.get(counter).getOp4());

    }


    private void onAuthSuccess(final FirebaseUser user) {

        if (user != null) {
            Intent i =getIntent();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot questionSnapshot = dataSnapshot.child("Question");//.child(Integer.toString(counter));
                       Iterable<DataSnapshot> questionChildren = questionSnapshot.getChildren();

                       //Takes all the questions from the database and creates a local arraylist of questions
                    for (DataSnapshot question : questionChildren) {
                        Question q = question.getValue(Question.class);
                        questions.add(q);
                    }
                        progressBar.setVisibility(View.GONE);
                        displayNextQuestion(0);

                    }



                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}

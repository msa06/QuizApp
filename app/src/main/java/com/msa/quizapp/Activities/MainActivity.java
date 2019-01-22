package com.msa.quizapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.msa.quizapp.Model.Question;
import com.msa.quizapp.Model.Quiz;
import com.msa.quizapp.Model.QuizStatus;
import com.msa.quizapp.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private CircularImageView mUserImage;
    private Button playbtn;
    private TextView gameplayed,quiz_name,quiz_time,quiz_price;
    private DatabaseReference mQuizStatusReference;
    private DatabaseReference mQuizReference;
    private DatabaseReference mQuestionsReference;
    private String liveStatus, showQuestion;
    private String currentQuesno = "1";
    private ValueEventListener mQuizStatusListner;
    private ValueEventListener mQuizListener;
    private ValueEventListener mQuestionListner;

    private List<Question> questionList;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intialise UI
        initUI();

        //Setting the Lisnter
        attachOnClickListners();

        attachQuizStatusListener();
        attachQuizListener();


    }

    private void attachOnClickListners() {
        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });

        //Auth Listner
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
            }
        };

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mQuestionListner == null) {
                    mQuestionListner = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot questionsnap : dataSnapshot.getChildren()) {
                                Question questions = questionsnap.getValue(Question.class);
                                questionList.add(questions);
                            }
                            updateQuestion(currentQuesno, questionList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    mQuestionsReference.addValueEventListener(mQuestionListner);
                }
            }
        });

        gameplayed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ResultActivity.class));
                finish();
            }
        });
    }

    private void initUI() {
        //Firebase Reference
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserImage = (CircularImageView) findViewById(R.id.user_image);
        playbtn = (Button) findViewById(R.id.play_btn);
        gameplayed = (TextView) findViewById(R.id.game_playeed_text);
        quiz_name = (TextView) findViewById(R.id.quiz_name);
        quiz_price = (TextView) findViewById(R.id.quiz_price);
        quiz_time = (TextView) findViewById(R.id.quiz_time);
        mQuestionsReference = FirebaseDatabase.getInstance().getReference().child("Question");
        mQuizReference = FirebaseDatabase.getInstance().getReference().child("Quizzes");
    }


    private void attachQuizListener() {
        if (mQuizListener == null) {
            mQuizListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Quiz quiz = dataSnapshot.getValue(Quiz.class);
                    updateQuizDetail(quiz);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mQuizReference.addValueEventListener(mQuizListener);
        }
    }

    private void updateQuizDetail(Quiz quiz) {
        quiz_name.setText(quiz.getQname());
    }

    private void attachQuizStatusListener() {
        if (mQuizStatusListner == null) {
            mQuizStatusListner = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    QuizStatus status = dataSnapshot.getValue(QuizStatus.class);
                    liveStatus = status.getLive();
                    currentQuesno = status.getCurques();
                    showQuestion = status.getShowques();
                    if (liveStatus == "0"){
                        playbtn.setText("Let's Play!!");
                    }
                    else
                        playbtn.setText("Not Live!!");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mQuizReference.addValueEventListener(mQuizListener);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        detachAllListener();
    }

    private void detachAllListener() {
        mAuth.removeAuthStateListener(mAuthListner);
        if(mQuestionListner!=null)
            mQuestionListner = null;
        if(mQuizListener!=null)
            mQuizListener = null;
        if(mQuizStatusListner!=null)
            mQuizStatusListner = null;

    }

}
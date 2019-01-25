package com.msa.quizapp.Activities;


import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msa.quizapp.Model.Question;
import com.msa.quizapp.R;

import java.util.ArrayList;
public class QuestionView extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    ProgressBar progressBar;
    FragmentManager fragman;
    CountDownTimer waitTimer;
    boolean fraginplace;
    int counter;
    boolean timeup=false;
    ArrayList<Question> questions;
    TextView questiionView, op1, op2, op3, op4,time;
    boolean clicked;
    Button nextQuestionbtn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_question_view, container, false);

        counter =  0;
        questions = new ArrayList<>();
        clicked = false;
        fraginplace=false;
        fragman= getFragmentManager();
        progressBar=(ProgressBar)v.findViewById(R.id.questionloadprog);
        questiionView = (TextView) v.findViewById(R.id.questionView);
        nextQuestionbtn = (Button) v.findViewById(R.id.nextQuestion);
        nextQuestionbtn.setEnabled(false);
        time=(TextView)v.findViewById(R.id.timeview);
        op1 = (TextView) v.findViewById(R.id.option1);
        op2 = (TextView) v.findViewById(R.id.option2);
        op3 = (TextView) v.findViewById(R.id.option3);
        op4 = (TextView) v.findViewById(R.id.option4);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        onAuthSuccess(mUser);
        nextQuestionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter < 4) {
                    //resets  colours
                    clicked = false;
                    timeup=false;
                    time.setBackgroundColor(getResources().getColor(R.color.white));

                    op1.setBackgroundResource(R.drawable.questionsholder);
                    op2.setBackgroundResource(R.drawable.questionsholder);
                    op3.setBackgroundResource(R.drawable.questionsholder);
                    op4.setBackgroundResource(R.drawable.questionsholder);
                    displayNextQuestion(++counter);
                    reverseTimer(5,time);
                }
            }
        });
        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markcorrectanswer(op1);
            }
        });
        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markcorrectanswer(op2);
            }
        });

        op3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markcorrectanswer(op3);
            }
        });
        op4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markcorrectanswer(op4);
            }
        });
        return v;
    }

    private void markcorrectanswer(TextView option) {

        //Checks if the answer is correct and colours the textview accordingly
        //To stop the timer
        if(waitTimer != null) {
            waitTimer.onFinish();
            //waitTimer.cancel();
            waitTimer = null;
        }
        TextView correctans = null;
        if (op1.getText().toString().equals(questions.get(counter).getAns())) {
            op1.setBackgroundResource(R.drawable.correctquestion);
            correctans = op1;
        } else if (op2.getText().toString().equals(questions.get(counter).getAns())) {
            op2.setBackgroundResource(R.drawable.correctquestion);
            correctans = op2;
        } else if (op3.getText().toString().equals(questions.get(counter).getAns())) {
            op3.setBackgroundResource(R.drawable.correctquestion);
            correctans = op3;
        } else if (op4.getText().toString().equals(questions.get(counter).getAns())) {
            op4.setBackgroundResource(R.drawable.correctquestion);
            correctans = op4;
        }

        if (correctans != null) {
            if (!clicked) {
                if (correctans != option) {
                    option.setBackgroundResource(R.drawable.wrongquestion);
                } else {
                    option.setBackgroundResource(R.drawable.correctquestion);
                }
            }
        } else
            System.out.println("Error occurred");

        clicked = true;

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
                    nextQuestionbtn.setEnabled(true);
                    displayNextQuestion(0);
                    progressBar.setVisibility(View.GONE);
                    reverseTimer(5,time);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public void reverseTimer(int Seconds, final TextView tv) {

        waitTimer = new CountDownTimer(Seconds * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                seconds = seconds % 60;
                tv.setText("Time:" + String.format("%02d", seconds));
            }

            public void onFinish() {
                //To prevent the timer from causing a npe from lack of a context
                int orientation = getActivity().getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    tv.setText("Completed");
                    tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    timeup = true;
                    if(waitTimer != null) {
                        waitTimer.cancel();
                        waitTimer = null;
                    }
                }
            }
        }.start();
    }
     public void onStart() {
        super.onStart();
            progressBar.setVisibility(View.VISIBLE);
            onAuthSuccess(mUser);
        }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(waitTimer != null) {
            waitTimer.cancel();
            waitTimer = null;
        }

    }
}

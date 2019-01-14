package com.msa.quizapp.Activities;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.msa.quizapp.Model.Quiz;
import com.msa.quizapp.R;

public class QuizActivity extends AppCompatActivity {
    private ImageView quiz_image;
    private TextView question_count;
    private TextView quiz_difficulty;
    private Button startbtn;
    private Toolbar mToolbar;
    private TextView mToolTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Referenceing Variable
        quiz_image = (ImageView) findViewById(R.id.quiz_image);
        question_count = (TextView) findViewById(R.id.question_count);
        quiz_difficulty = (TextView) findViewById(R.id.quiz_difficulty);
        startbtn = (Button) findViewById(R.id.start_btn);

        //Set Toolbar
        //Setting Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolTitle = (TextView) findViewById(R.id.toolbar_title);

        setSupportActionBar(mToolbar);


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Getting Quiz Data
        Bundle data = getIntent().getExtras();
        final Quiz currentquiz = (Quiz) data.getParcelable("SelectedQuiz"); //currentquiz position

        //Setting Values
        Glide.with(this)
                .load(currentquiz.getQimageurl())
                .into(quiz_image);
        mToolTitle.setText(currentquiz.getQname());
        question_count.setText(currentquiz.getQamount());
        quiz_difficulty.setText(currentquiz.getQdifficulty());

        //Button OnCLick Listner
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Intent and pass qid to fetch question in questionAreaActivity
                Intent quizIntent = new Intent(QuizActivity.this,QuestionAreaActivity.class);
                quizIntent.putExtra("CurrentQuiz",currentquiz);
                startActivity(quizIntent);
                finish();
            }
        });

    }
}

package com.msa.quizapp.Activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.msa.quizapp.Model.Question;
import com.msa.quizapp.Model.Quiz;
import com.msa.quizapp.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionAreaActivity extends AppCompatActivity {

    //Variable Declaration
    private CountDownTimer countDownTimer;
    private Toolbar mToolbar;
    private TextView mToolTitle;
    private TextView questionArea;
    private Button option1,option2,option3,option4,next_btn;
    private int currentQuestionNo=0;
    private List<Question> questionList;
    private  Quiz currentquiz;
    private static final int QUESTION_LOADER_ID = 1;
    private String QUIZ_URL;
    private String selectedAnswer;
    private Question currentQuestion;
    private int result=0;
    private TextView countdowntext1;
    private  long timeleftinmillisec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_area);

        //Getting Quiz Data
        Bundle data = getIntent().getExtras();
        currentquiz = (Quiz) data.getParcelable("CurrentQuiz");

        //Set Current URL
        QUIZ_URL = currentquiz.getQurl();

        //Set Toolbar
        //Setting Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolTitle = (TextView) findViewById(R.id.question_count);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //Refrencing The Variable
        questionArea = (TextView) findViewById(R.id.question_area);
        option1 = (Button) findViewById(R.id.option_1);
        option2 = (Button) findViewById(R.id.option_2);
        option3 = (Button) findViewById(R.id.option_3);
        option4 = (Button) findViewById(R.id.option_4);
        next_btn = (Button) findViewById(R.id.next_btn);
        countdowntext1=(TextView) findViewById(R.id.coundowntext);

        countDown();

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswer = option1.getText().toString();
                changeButtonState(option1);
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswer = option2.getText().toString();
                changeButtonState(option2);

            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswer = option3.getText().toString();
                changeButtonState(option3);
            }
        });
        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswer = option4.getText().toString();
                changeButtonState(option4);
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for the Current Answer
                result = checkAnswer(selectedAnswer,currentQuestion.getCorrectanswer());
                //Increment the current question No and Update UI
                countDownTimer.cancel();
                countDown();
                if(currentQuestionNo < Integer.parseInt(currentquiz.getQamount())-1){
                    updateQuestion(++currentQuestionNo);

                }
                else{
                    Intent resultIntent = new Intent(QuestionAreaActivity.this,ResultActivity.class);
                    resultIntent.putExtra("Result",result);
                    startActivity(resultIntent);

                }

            }
        });



    }

    private void countDown() {
        timeleftinmillisec=20000;
        countDownTimer= new CountDownTimer(timeleftinmillisec,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleftinmillisec=millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }

    public void updateTimer()
    {
        int minutes=(int) timeleftinmillisec/60000;
        int seconds=(int) timeleftinmillisec%60000/1000;
        String timerLeftText;
        timerLeftText ="" +minutes;
        timerLeftText+=":";
        if(seconds<10) timerLeftText+="0";
        timerLeftText += seconds;
        countdowntext1.setText(timerLeftText);
        next_btn.setEnabled(false);
        next_btn.setBackgroundColor(getResources().getColor(R.color.red));
     if(seconds<1) {
          next_btn.setEnabled(true);
           next_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }

    }


    private void changeButtonState(Button selectedbtn) {
        //Disable All button
        setEnableButtons(false);
       //Color the button appopriatley
        String btn1ans = option1.getText().toString();
        String btn2ans = option2.getText().toString();
        String btn3ans = option3.getText().toString();
        String btn4ans = option4.getText().toString();
        String correctans = currentQuestion.getCorrectanswer();
        if(selectedbtn.getText().toString() == correctans){
            changeColor(selectedbtn,R.drawable.selectedbtnright,R.color.white);
        }
        else{
            changeColor(selectedbtn,R.drawable.selectedbtnwrong,R.color.white);
        }


    }


    private void setEnableButtons(boolean b) {
        option1.setEnabled(b);
        option2.setEnabled(b);
        option3.setEnabled(b);
        option4.setEnabled(b);
    }


    private void changeColor(Button selectedbtn, int drawableid,int colorid) {
        selectedbtn.setTextColor(getResources().getColor(colorid));
        selectedbtn.setBackground(getResources().getDrawable(drawableid));
        InputStream in;
    }


    private int checkAnswer(String selectedAnswer, String correctanswer) {
        //if User hasnot Selected The answer
        if(selectedAnswer==null){
            return result;
        }
        if(selectedAnswer.equals(correctanswer)){
            result = result + 1;
        }
        return result;
    }


    private void updateQuestion(int currentQuestionNo) {
          if(questionList!=null){
              currentQuestion = questionList.get(currentQuestionNo);
            //Update the UI with CurrentQuestion
            updateUI(currentQuestion);

          }
    }

    private void updateUI(Question currentQuestion) {
        mToolTitle.setText(""+(currentQuestionNo+1) + "/" + questionList.size());
        questionArea.setText(currentQuestion.getQuestion());
        setEnableButtons(true);
        //Button Color
        changeColor(option1,R.drawable.option_btn,R.color.black);
        changeColor(option2,R.drawable.option_btn,R.color.black);
        changeColor(option3,R.drawable.option_btn,R.color.black);
        changeColor(option4,R.drawable.option_btn,R.color.black);

        ArrayList<String> options = currentQuestion.getOptions();
        Collections.shuffle(options);

        option1.setText(options.get(0));
        option2.setText(options.get(1));
        option3.setText(options.get(2));
        option4.setText(options.get(3));

    }

}

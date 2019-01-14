package com.msa.quizapp.Model;

import java.util.ArrayList;

public class Question {
    private String question;
    private String correctanswer;
    private ArrayList<String> options= new ArrayList<>();
    public Question() {
    }

    public Question(String question, String correctanswer, ArrayList<String> options) {
        this.question = question;
        this.correctanswer = correctanswer;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectanswer() {
        return correctanswer;
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}

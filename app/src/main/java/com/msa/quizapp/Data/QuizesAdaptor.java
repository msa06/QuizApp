package com.msa.quizapp.Data;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.msa.quizapp.Model.Quiz;
import com.msa.quizapp.R;

import java.util.List;

public class QuizesAdaptor extends ArrayAdapter<Quiz> {
    private Activity context;
    private List<Quiz> quizList;

    public QuizesAdaptor(Activity context,List<Quiz> quizList) {
        super(context, R.layout.quiz_row, quizList);
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View itemListView = inflater.inflate(R.layout.quiz_row,null,true);

        ImageView quiz_image = (ImageView) itemListView.findViewById(R.id.quiz_image);
        TextView quiz_text = (TextView) itemListView.findViewById(R.id.quiz_name);

        Quiz quiz = quizList.get(position);

        //Set Values
        quiz_text.setText(quiz.getQname());
        Glide.with(context)
                .load(quiz.getQimageurl())
                .into(quiz_image);

        return  itemListView;
    }
}

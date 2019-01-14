package com.msa.quizapp.Utils;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.msa.quizapp.Model.Question;

import java.util.List;

public class QuestionLoader extends AsyncTaskLoader<List<Question>> {
    private String quizURL;


    public QuestionLoader(Context context, String url) {
        super(context);
        this.quizURL = url;
    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    @Override
    public List<Question> loadInBackground() {

        // Don't perform the request if there are no URLs, or the first URL is null.
        if (quizURL == null) {
            return null;
        }

        List<Question> result = QueryUtils.fetchEarthquakeData(quizURL);
        return result;
    }
}

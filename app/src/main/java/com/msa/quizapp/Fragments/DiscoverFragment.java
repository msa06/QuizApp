package com.msa.quizapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msa.quizapp.Activities.QuizActivity;
import com.msa.quizapp.Data.QuizesAdaptor;
import com.msa.quizapp.Model.Quiz;
import com.msa.quizapp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    private DatabaseReference categoiesReference;
    private ValueEventListener mValueEventListner;
    List<Quiz> quizcategories;
    private ListView listView;
    private QuizesAdaptor mQuizesAdaptor;
    private static String TAG = "DiscoverFragment";
    Toolbar mToolbar;
    TextView mToolTitle;

    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_discover, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        categoiesReference = FirebaseDatabase.getInstance().getReference().child("Quizes");
        quizcategories = new ArrayList<>();
        mQuizesAdaptor = new QuizesAdaptor(getActivity(),quizcategories);
        listView.setAdapter(mQuizesAdaptor);

        //Referencing Progress Bar

                //Set Toolbar
        //Setting Toolbar
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolTitle = (TextView) view.findViewById(R.id.toolbar_title);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolTitle.setText(mToolbar.getTitle());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        //On Long Press SHow the Option
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Quiz selectedquiz = quizcategories.get(position);
                Intent quizIntent = new Intent(getActivity(),QuizActivity.class);
                quizIntent.putExtra("SelectedQuiz",selectedquiz);
                startActivity(quizIntent);

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        attachDatabaseReadListner();
    }

    private void attachDatabaseReadListner() {
        if(quizcategories.size()>0){
            quizcategories.clear();
        }
        if(mValueEventListner == null){
            mValueEventListner = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progress_indicator);
                    progressBar.setVisibility(View.GONE);
                    for(DataSnapshot categoriesSnapshot: dataSnapshot.getChildren()){
                        Quiz quiz = categoriesSnapshot.getValue(Quiz.class);
                        mQuizesAdaptor.add(quiz);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        }

        categoiesReference.addValueEventListener(mValueEventListner);
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListner();
        mQuizesAdaptor.clear();
    }

    private void detachDatabaseReadListner() {
        if(mValueEventListner!=null){
            categoiesReference.removeEventListener(mValueEventListner);
            mValueEventListner = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListner();
    }
}

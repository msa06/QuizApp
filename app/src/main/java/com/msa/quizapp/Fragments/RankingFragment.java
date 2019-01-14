package com.msa.quizapp.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msa.quizapp.Data.QuizesAdaptor;
import com.msa.quizapp.Data.RankAdaptor;
import com.msa.quizapp.Model.Quiz;
import com.msa.quizapp.Model.Rank;
import com.msa.quizapp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RankingFragment extends Fragment {

    private Toolbar mToolbar;
    private TextView mToolTitle;
    private DatabaseReference rankingReference;
    private ValueEventListener mValueEventListner;
    List<Rank> rankList;
    private ListView listView;
    private RankAdaptor mRankAdaptor;
    private static String TAG = "DiscoverFragment";

    public RankingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_ranking, container, false);

        //Set Toolbar
        //Setting Toolbar
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolTitle = (TextView) view.findViewById(R.id.toolbar_title);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolTitle.setText(mToolbar.getTitle());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Referencing
        listView = (ListView) view.findViewById(R.id.listview);
        rankingReference = FirebaseDatabase.getInstance().getReference().child("Ranking");
        rankList = new ArrayList<>();
        mRankAdaptor = new RankAdaptor(getActivity(),rankList);
        listView.setAdapter(mRankAdaptor);

        return  view;
    }
    @Override
    public void onStart() {
        super.onStart();
        attachDatabaseReadListner();
    }

    private void attachDatabaseReadListner() {
        if(mValueEventListner == null){
            mValueEventListner = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progress_indicator);
                    progressBar.setVisibility(View.GONE);
                    for(DataSnapshot rankSnapshot: dataSnapshot.getChildren()){
                        Rank rank = rankSnapshot.getValue(Rank.class);
                        mRankAdaptor.add(rank);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        }

        rankingReference.addValueEventListener(mValueEventListner);
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListner();
        mRankAdaptor.clear();
    }

    private void detachDatabaseReadListner() {
        if(mValueEventListner!=null){
            rankingReference.removeEventListener(mValueEventListner);
            mValueEventListner = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListner();
    }

}

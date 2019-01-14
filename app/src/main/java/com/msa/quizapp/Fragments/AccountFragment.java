package com.msa.quizapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.msa.quizapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private Button logoutBtn;
    private Toolbar mToolbar;
    private TextView mToolTitle;
    private CircularImageView userimage;
    private TextView username;
    private TextView useremail;
    //Firebase
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabaseReference;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_account, container, false);

        //Firebase Reference
        mAuth = FirebaseAuth.getInstance();
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mUser = mAuth.getCurrentUser();

        //Reference
        logoutBtn = (Button) view.findViewById(R.id.log_out);
        userimage = (CircularImageView) view.findViewById(R.id.user_circular_image);
        username = (TextView) view.findViewById(R.id.user_name);
        useremail = (TextView) view.findViewById(R.id.user_email);

        //Set Toolbar
        //Setting Toolbar
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolTitle = (TextView) view.findViewById(R.id.toolbar_title);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolTitle.setText(mToolbar.getTitle());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        //Setting User Info
        Glide.with(getActivity())
                .load(mUser.getPhotoUrl())
                .into(userimage);
        username.setText(mUser.getDisplayName());
        useremail.setText(mUser.getEmail());

        return view;
    }

}

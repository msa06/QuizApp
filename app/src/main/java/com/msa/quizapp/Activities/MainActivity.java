package com.msa.quizapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msa.quizapp.Fragments.AccountFragment;
import com.msa.quizapp.Fragments.DiscoverFragment;
import com.msa.quizapp.Fragments.RankingFragment;
import com.msa.quizapp.Model.User;
import com.msa.quizapp.R;

public class MainActivity extends AppCompatActivity {


    //Main Activity
    BottomNavigationView mBottomNav;
    FrameLayout mMainFrame;

    DiscoverFragment discoverFragment;
    RankingFragment rankingFragment;
    AccountFragment accountFragment;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListner;



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise
        mBottomNav = (BottomNavigationView) findViewById(R.id.nav_bottom);
        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);

        //Fragment Constructor
        discoverFragment = new DiscoverFragment();
        rankingFragment = new RankingFragment();
        accountFragment = new AccountFragment();
        setFragment(discoverFragment);
        setTitle("Discover");

        //Firebase Reference
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


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

        //OnItemClickListner On Bottom Navigation
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_discover:
                        setFragment(discoverFragment);
                        setTitle("Discover");
                        return true;

                    case R.id.nav_ranking:
                        setFragment(rankingFragment);
                        setTitle("Ranking");
                        return true;

                    case R.id.nav_account:
                        setFragment(accountFragment);
                        setTitle("Account");
                        return true;

                    default:
                        return false;
                }
            }
        });

    }

    //SetFragment
    public void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListner);
    }




}
package com.msa.quizapp.Activities;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {


    private static final String TAG = "MyFirebaseIIDService";
    
    @Override
    public void onNewToken(String s) {
        // Get updated InstanceID token.

        super.onNewToken(s);
        Log.d("NEW_TOKEN", s);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}

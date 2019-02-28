package com.example.edson.pushfatecadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {


        private FirebaseAuth mAuth;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Registrar o usuario em um topico(curso)
        FirebaseMessaging.getInstance().subscribeToTopic("ADS");

        if (currentUser == null) {
            sendToLogin();
        }else{
            sendToSend();
        }
    }

    private void sendToSend() {
        Intent mainIntent = new Intent(MainActivity.this, SendActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }
}

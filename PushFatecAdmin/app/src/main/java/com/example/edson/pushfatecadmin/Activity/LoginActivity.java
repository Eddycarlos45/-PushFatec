package com.example.edson.pushfatecadmin.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.edson.pushfatecadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText emailedt;
    private EditText senhaedt;
    private Button loginbtn;
    private Button registrobtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailedt = findViewById(R.id.login_email);
        senhaedt = findViewById(R.id.login_password);
        loginbtn = findViewById(R.id.login_btn);
        registrobtn = findViewById(R.id.login_register_btn);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        registrobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registroIntent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(registroIntent);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailedt.getText().toString();
                String senha = senhaedt.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)) {
                    mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String token_id = FirebaseInstanceId.getInstance().getToken();
                                String current_id = mAuth.getCurrentUser().getUid();

                                Map<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("token_id", token_id);

                                mFirestore.collection("Users").document(current_id).update(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        sendToMenu();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Error:" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });


    }

    private void sendToMenu() {
        Intent menuIntent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(menuIntent);
        finish();
    }
}

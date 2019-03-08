package com.example.edson.pushfateccliente;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.edson.pushfateccliente.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    //Verificar se o usuario corrente ja esta cadastrado usando o FirebaseAuth
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        }else{
            sendToMenu();
        }
    }
    //Metodo para ir ao Menu
    private void sendToMenu() {
        Intent menuIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(menuIntent);
        finish();

    }
    //Metodo para ir ao Login
    private void sendToLogin() {
        Intent loginIntent = new Intent(LoginActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
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
        //Metodo para fazer Login no App
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailedt.getText().toString();
                String senha = senhaedt.getText().toString();
                //Verificando os campos de email e login nao estao nulos
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)) {
                    //Verificando se o usuario ja esta cadastrado no App
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
                                        sendToSend();
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

    private void sendToSend() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}

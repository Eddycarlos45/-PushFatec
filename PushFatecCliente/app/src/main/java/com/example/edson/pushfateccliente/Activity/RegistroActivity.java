package com.example.edson.pushfateccliente.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.edson.pushfateccliente.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText emailedt;
    private EditText senhaedt;
    private EditText nomeedt;
    private Button registrobtn;
    private Button loginbtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    Spinner spinner_curso;
    private String curso = "";
    private EditText confSenhaedt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Definir orientação como portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        emailedt = findViewById(R.id.register_email);
        senhaedt = findViewById(R.id.register_password);
        nomeedt = findViewById(R.id.register_name);
        registrobtn = findViewById(R.id.register_btn);
        spinner_curso = findViewById(R.id.spinner_curso);
        confSenhaedt = findViewById(R.id.confirm_password);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cursos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_curso.setAdapter(adapter);
        spinner_curso.setOnItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        registrobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nomeedt.getText().toString();
                String email = emailedt.getText().toString();
                String senha = senhaedt.getText().toString();
                curso = String.valueOf(spinner_curso.getSelectedItem());
                String confSenha = confSenhaedt.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha) && !TextUtils.isEmpty(curso) && !TextUtils.isEmpty(confSenha) && senha.equals(confSenha)) {

                    mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final String user_id = mAuth.getCurrentUser().getUid();
                                String token_id = FirebaseInstanceId.getInstance().getToken();

                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("nome", name);
                                userMap.put("token_id", token_id);
                                userMap.put("curso", curso);

                                mFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "Conta criada com sucesso!", Toast.LENGTH_LONG).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Error:" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    validarCampos();
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        curso = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void validarCampos() {
        if (nomeedt.getText().toString().isEmpty()) {
            nomeedt.setError("Digite seu nome");
        }
        if (emailedt.getText().toString().isEmpty()) {
            emailedt.setError("Digite seu email");
        }
        if (senhaedt.getText().toString().isEmpty()) {
            senhaedt.setError("Escolha uma senha com mais de 6 caracteres");
        }
        if (confSenhaedt.getText().toString().isEmpty()) {
            confSenhaedt.setError("Confirme sua senha");
        }if(!senhaedt.getText().toString().equals(confSenhaedt.getText().toString())){
            senhaedt.setError("Confira sua senha");
            senhaedt.setError("Confirme sua senha");
        }



    }
}
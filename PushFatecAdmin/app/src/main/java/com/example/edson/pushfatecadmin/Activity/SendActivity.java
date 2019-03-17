package com.example.edson.pushfatecadmin.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edson.pushfatecadmin.R;
import com.example.edson.pushfatecadmin.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {

    private EditText mensagem_edt;
    private Button btnEnviar;
    private FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    private String topico;
    private EditText titulo_edt;
    private Spinner spinner;
    private String autor;
    private  String data_completa;
    private String  hora_atual;



    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        mensagem_edt = findViewById(R.id.message_view);
        btnEnviar = findViewById(R.id.send_btn);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        titulo_edt = findViewById(R.id.titulo_view);
        spinner = findViewById(R.id.spinner_curso);

        deleteDatabase("mensagens.db");
        recuperarNome();
        capturarHorario();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mensagem = mensagem_edt.getText().toString();
                String titulo = titulo_edt.getText().toString();
                String curso = spinner.getSelectedItem().toString();


                if (!TextUtils.isEmpty(mensagem) && !TextUtils.isEmpty(titulo) && !TextUtils.isEmpty(curso)) {
                    iniciarNotificacao();
                    salvarMensagem();
                } else {
                    Toast.makeText(getApplicationContext(), "Preencher todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    private void recuperarNome() {
        DocumentReference docRef = mFirestore.collection("Documents").document("Users");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        autor = document.getData().get("nome").toString();

                    } else {
                        Log.d("-----", "No such document");
                    }
                } else {
                    Log.d("-----", "get failed with ", task.getException());
                }
            }
        });
    }


    private void iniciarNotificacao() {


        JSONObject notificacao = new JSONObject();
        JSONObject dados = new JSONObject();

        String mensagem = mensagem_edt.getText().toString();
        String titulo = titulo_edt.getText().toString();

        try {
            notificacao.put("to", "/topics/" + spinner.getSelectedItem().toString());
            dados.put("mensagem", mensagem);
            dados.put("titulo", titulo);
            dados.put("nome", autor);
            dados.put("urlimagem", "https://images.g2crowd.com/uploads/product/image/large_detail/large_detail_1490630196/firebase.png");

            notificacao.put("data", dados);

            String site = "https://fcm.googleapis.com/fcm/send";

            enviarNotificacao(site, notificacao);


        } catch (Exception e) {

        }
    }

    private void enviarNotificacao(String site, JSONObject notificacao) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, site, notificacao,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })

        {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();

                header.put("Authorization", "key=AAAAegT227M:APA91bGANmoleGg-k7xeAdLW5BV_AierfVBTcHEt8v03bixsBJ4fvNBwJFwUqtUahp8lhRr-i3e0CrMgRnMBMxG8dncfate-Hwk2jTy2huVHc17ic3aVWlXcP1uiMId3KezXDDE1F9ML");
                header.put("Content-Type", "application/json");

                return header;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

    public void salvarMensagem() {


        SQLiteDatabase myDB = openOrCreateDatabase("mensagens.db", MODE_PRIVATE, null);

        myDB.execSQL("CREATE TABLE IF NOT EXISTS mensagens (" +
                "idmensagem INTEGER PRIMARY KEY AUTOINCREMENT," +
                "autor VARCHAR(50)," +
                "mensagem VARCHAR(1000)," +
                "titulo VARCHAR(100)," +
                "horario VARCHAR(30))");



        ContentValues row1 = new ContentValues();

        row1.put("autor", autor);
        row1.put("mensagem", mensagem_edt.getText().toString());
        row1.put("titulo", titulo_edt.getText().toString());
        row1.put("horario", data_completa);

        myDB.insert("mensagens", null, row1);

        myDB.close();


    }
    public void capturarHorario(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        data_completa = dateFormat.format(data_atual);

        hora_atual  = dateFormat_hora.format(data_atual);


    }


    }


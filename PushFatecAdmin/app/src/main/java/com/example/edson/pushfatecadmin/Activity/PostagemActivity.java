package com.example.edson.pushfatecadmin.Activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.edson.pushfatecadmin.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PostagemActivity extends AppCompatActivity {

    private Bitmap imagem;
    private Button selecionarbtn;
    private Button enviarbtn;
    private final int Selecao_galeria = 100;
    private ImageView imagemview;
    private String Urldownload;
    private String caminhoImagem;

    private FirebaseFirestore mFirestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postagem);

        //Definir orientação como portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gerarCaminho(1000000);
        imagemview = findViewById(R.id.imageView2);
        selecionarbtn = findViewById(R.id.selecionar_imagem_btn);
        enviarbtn = findViewById(R.id.salvar_imagem_btn);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mFirestore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        selecionarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();

            }
        });

        enviarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarImagem();
            }
        });
    }


    private void selecionarImagem() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //Caso não exista uma galeria de fotos

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, Selecao_galeria);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                Uri localImagem = data.getData();

                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagem);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (imagem != null) {
                imagemview.setImageBitmap(imagem);

            }
        }
    }

    private void salvarImagem() {
        //Recuperar dados da imagem para o firebase

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        final byte[] dadosImagem = baos.toByteArray();


        final StorageReference imagemRef = storageReference
                .child("imagem")
                .child("postagens")
                .child(caminhoImagem);


        final UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Erro ao fazer upload da imagem", Toast.LENGTH_LONG).show();

            }

        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Sucesso ao fazer upload da imagem", Toast.LENGTH_LONG).show();
                recuperarUrl();
                gerarCaminho(10000000);

            }
        });
    }

    private void recuperarUrl() {
        final Task<Uri> ref = storageReference.child("imagem/postagens/" + caminhoImagem)
                .getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Urldownload = task.getResult().toString();
                        Log.d("XXXXXXXXXX", Urldownload);
                        salvarUrl(Urldownload);
                    }
                });


    }

    private void salvarUrl(String urldownload) {


        Map<String, Object> postagens = new HashMap<>();
        postagens.put("imagem", Urldownload);

        mFirestore.collection("Postagens").document(caminhoImagem).set(postagens)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("-------", "Sucesso");

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Erro: ", e.getMessage());
            }
        });
    }


    //Gerar um caminho para imagem
    public void gerarCaminho(int rand) {
        Random random = new Random();
        caminhoImagem = "local" + random.nextInt(rand) + ".jpeg";
    }
}
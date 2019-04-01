package com.example.edson.pushfatecadmin.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.edson.pushfatecadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class PostagemActivity extends AppCompatActivity {

    private Bitmap imagem;
    private Button selecionarbtn;
    private Button enviarbtn;
    private final int Selecao_galeria = 100;
    private ImageView imagemview;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postagem);

        //Definir orientação como portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        imagemview = findViewById(R.id.imageView2);
        selecionarbtn = findViewById(R.id.selecionar_imagem_btn);
        enviarbtn = findViewById(R.id.salvar_imagem_btn);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();


        selecionarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarImagem();
            }
        });

        enviarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarImagem();

            }
        });
    }

    private void enviarImagem() {
        salvarImagem();

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
        byte[] dadosImagem = baos.toByteArray();

        StorageReference imagemRef = storageReference
                .child("imagem")
                .child("postagens")
                .child("postagens.jpeg");

        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Erro ao fazer upload da imagem", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Sucesso ao fazer upload da imagem", Toast.LENGTH_LONG).show();
            }
        });
    }


}
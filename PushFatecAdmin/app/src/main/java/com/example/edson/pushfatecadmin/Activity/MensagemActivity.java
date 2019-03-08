package com.example.edson.pushfatecadmin.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.edson.pushfatecadmin.Adapter.AdapterMensagens;
import com.example.edson.pushfatecadmin.Model.Mensagem;
import com.example.edson.pushfatecadmin.R;

import java.util.ArrayList;
import java.util.List;

public class MensagemActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Mensagem> listaMensagem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);


        recyclerView = findViewById(R.id.recyclerMensagens);

        //Lista de Mensagens
        criarMensagens();

        //Congigurar Adapter

        AdapterMensagens adapterMensagens = new AdapterMensagens(listaMensagem);


        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMensagens);
    }

    public void criarMensagens() {
        SQLiteDatabase myDB = openOrCreateDatabase("mensagens.db", MODE_PRIVATE, null);

        Cursor myCursor = myDB.rawQuery("select autor, mensagem, titulo from mensagens", null);

        while (myCursor.moveToNext()) {
            String autor_db = "De: " + myCursor.getString(0);
            String mensagem_db = "Mensagem: " + myCursor.getString(1);
            String titulo_db = "Título: "+myCursor.getString(2);

            Mensagem mensagem = new Mensagem(autor_db, mensagem_db, titulo_db);
            this.listaMensagem.add(mensagem);


        }
        myCursor.close();
        myDB.close();


    }


}

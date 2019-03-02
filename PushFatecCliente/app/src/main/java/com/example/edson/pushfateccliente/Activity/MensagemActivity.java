package com.example.edson.pushfateccliente.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.edson.pushfateccliente.Adapter.AdapterMensagens;
import com.example.edson.pushfateccliente.Model.Mensagem;
import com.example.edson.pushfateccliente.R;

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
        listarMensagens();

        //Congigurar Adapter

        AdapterMensagens adapterMensagens = new AdapterMensagens(listaMensagem);


        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMensagens);
    }

    public void listarMensagens() {
        SQLiteDatabase myDB = openOrCreateDatabase("mensagens.db", MODE_PRIVATE, null);

        Cursor myCursor = myDB.rawQuery("select autor, mensagem, titulo from mensagens", null);

        while (myCursor.moveToNext()) {
            String autor_db = myCursor.getString(0);
            String mensagem_db = myCursor.getString(1);
            String titulo_db = myCursor.getString(2);

            Mensagem mensagem = new Mensagem(autor_db, mensagem_db, titulo_db);
            this.listaMensagem.add(mensagem);


        }
        myCursor.close();
        myDB.close();


    }


}

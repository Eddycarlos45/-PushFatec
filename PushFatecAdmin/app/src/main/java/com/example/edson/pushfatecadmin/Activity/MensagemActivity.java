package com.example.edson.pushfatecadmin.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edson.pushfatecadmin.Adapter.AdapterMensagens;
import com.example.edson.pushfatecadmin.Model.Mensagem;
import com.example.edson.pushfatecadmin.R;
import com.example.edson.pushfatecadmin.Util.RecyclerItemClickListener;

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

        //Evento de Click
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(final View view, final int position) {

                            }

                            @Override
                            public void onLongItemClick(final View view, int position) {
                                final Integer idremover = listaMensagem.get(position).getIdmensagem();

                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle("Confirmação")
                                        .setMessage("Tem certeza que deseja excluir esta mensagem?")
                                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                                            @Override

                                            public void onClick(DialogInterface dialog, int which) {
                                                boolean sucesso = removerMensagem(idremover);
                                                if (sucesso) {
                                                    removerMensagem(idremover);
                                                    Snackbar.make(view, "Excluiu!", Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                } else {
                                                    Snackbar.make(view, "Erro ao excluir o mensagem!", Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                }
                                            }
                                        })
                                        .setNegativeButton("Cancelar", null)
                                        .create()
                                        .show();


                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }


    public void listarMensagens() {
        SQLiteDatabase myDB = openOrCreateDatabase("mensagens.db", MODE_PRIVATE, null);

        Cursor myCursor = myDB.rawQuery("select * from mensagens", null);

        for (myCursor.moveToLast(); !myCursor.isBeforeFirst(); myCursor.moveToPrevious()) {
            Integer id_db = myCursor.getInt(0);
            String autor_db = "De: " + myCursor.getString(1);
            String mensagem_db = "Mensagem: " + myCursor.getString(2);
            String titulo_db = "Título: " + myCursor.getString(3);
            //  String horario_db = myCursor.getString(4);

            Mensagem mensagem = new Mensagem(id_db, autor_db, mensagem_db, titulo_db);
            this.listaMensagem.add(mensagem);

        }
        myCursor.close();
        myDB.close();
    }
    public boolean removerMensagem(Integer id) {
        SQLiteDatabase myDB = openOrCreateDatabase("mensagens.db", MODE_PRIVATE, null);

        myDB.delete("mensagens", " idmensagem =  " + id, null);
        Toast.makeText(getApplicationContext(), "Mensagem excluida ", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(MensagemActivity.this, MenuActivity.class);
        startActivity(intent);


        myDB.close();

        return false;

    }



}

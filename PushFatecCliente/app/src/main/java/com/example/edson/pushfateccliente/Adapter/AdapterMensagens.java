package com.example.edson.pushfateccliente.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.edson.pushfateccliente.Model.Mensagem;
import com.example.edson.pushfateccliente.R;

import java.util.List;

public class AdapterMensagens extends RecyclerView.Adapter<AdapterMensagens.MyViewHolder> {

    private List<Mensagem> listaMensagens;


    public AdapterMensagens(List<Mensagem> lista) {
        this.listaMensagens = lista;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_lista, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Mensagem mensagem = listaMensagens.get(position);
        holder.mensagem.setText(mensagem.getMensagem());
        holder.titulo.setText(mensagem.getTitulo());
        holder.autor.setText(mensagem.getAutor());
        holder.horario.setText(mensagem.getHorario());



    }

    @Override
    public int getItemCount() {
        return listaMensagens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView autor;
        TextView mensagem;
        TextView horario;


        public MyViewHolder(View itemView) {

            super(itemView);

            titulo = itemView.findViewById(R.id.text_titulo);
            autor = itemView.findViewById(R.id.text_autor);
            mensagem = itemView.findViewById(R.id.text_mensagem);
            horario = itemView.findViewById(R.id.text_horario);
        }
    }
}

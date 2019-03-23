package com.example.edson.pushfatecadmin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.edson.pushfatecadmin.Model.Postagem;
import com.example.edson.pushfatecadmin.R;

import java.util.List;

public class AdapterPostagens extends RecyclerView.Adapter<AdapterPostagens.MyViewHolder> {

    private List<Postagem> postagens;

    public AdapterPostagens(List<Postagem> listaPostagens) {
        this.postagens = listaPostagens;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemList = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postagem_detalhe, parent, false);

        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Postagem postagem = postagens.get(position);
        holder.nome.setText(postagem.getNome());
        holder.imagem.setImageResource(postagem.getImagem());


    }

    @Override
    public int getItemCount() {
        return postagens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nome;
        private ImageView imagem;




        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textNome);
            imagem = itemView.findViewById(R.id.imageView);
        }


    }
}

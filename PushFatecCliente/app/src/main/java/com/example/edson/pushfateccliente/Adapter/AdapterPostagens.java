package com.example.edson.pushfateccliente.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.edson.pushfateccliente.Model.Postagem;
import com.example.edson.pushfateccliente.R;

import java.util.List;

public class AdapterPostagens extends RecyclerView.Adapter<AdapterPostagens.MyViewHolder> {

    private List<Postagem> postagens;
    private Context context;

    public AdapterPostagens(List<Postagem> postagens, Context context) {
        this.postagens = postagens;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemList = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postagem_detalhe, parent, false);

        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ImageView imageView = holder.imagem;
        Glide.with(context).load(postagens.get(position).getImagem()).into(imageView);


    }

    @Override
    public int getItemCount() {
        return postagens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
      ;
        private View mView;
        private ImageView imagem;


        public MyViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            imagem = itemView.findViewById(R.id.imageView);
        }


    }
}

package com.example.edson.pushfatecadmin.Model;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.RequestBuilder;

import java.net.URL;

public class Postagem {


    private int imagem;

    public Postagem() {
    }

    public Postagem( int imagem) {

        this.imagem = imagem;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }


}

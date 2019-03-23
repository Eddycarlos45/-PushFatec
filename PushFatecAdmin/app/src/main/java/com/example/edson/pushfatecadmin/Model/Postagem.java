package com.example.edson.pushfatecadmin.Model;

public class Postagem {

    private String nome;
    private int imagem;

    public Postagem() {
    }

    public Postagem(String nome, int imagem) {
        this.nome = nome;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }


}

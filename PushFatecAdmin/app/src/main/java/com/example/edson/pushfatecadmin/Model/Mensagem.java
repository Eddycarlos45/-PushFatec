package com.example.edson.pushfatecadmin.Model;

public class Mensagem {

    private String autor;
    private String mensagem;
    private String titulo;

    public Integer getIdmensagem() {
        return idmensagem;
    }

    public void setIdmensagem(Integer idmensagem) {
        this.idmensagem = idmensagem;
    }

    private Integer idmensagem;

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Mensagem() {
    }

    public Mensagem(Integer idmensagem, String autor, String mensagem, String titulo) {
        this.idmensagem = idmensagem;
        this.autor = autor;
        this.mensagem = mensagem;
        this.titulo = titulo;

    }


}

package com.api.models;

public class Candidato {

    private int id;
    private String nome;
    private String partido;
    private String categoria;
    private int numero;

    public Candidato() {}

    public Candidato(int id, String nome, String partido, String categoria, int numero) {
        this.id = id;
        this.nome = nome;
        this.partido = partido;
        this.categoria = categoria;
        this.numero = numero;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPartido() { return partido; }
    public void setPartido(String partido) { this.partido = partido; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
}
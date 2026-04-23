package com.api.models;

public class Voto {
    private int id;
    private int idEleitor;
    private int numeroCandidato;
    private String nomeEleitor;
    private String nomeCandidato;

    public Voto() {

    }

    public int getId() {
        return id;
    }

    public int getIdEleitor() {
        return idEleitor;
    }

    public String getNomeEleitor() {
        return nomeEleitor;
    }

    public int getNumeroCandidato() {
        return numeroCandidato;
    }

    public String getNomeCandidato() {
        return nomeCandidato;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdEleitor(int idEleitor) {
        this.idEleitor = idEleitor;
    }

    public void setNomeCandidato(String nomeCandidato) {
        this.nomeCandidato = nomeCandidato;
    }

    public void setNumeroCandidato(int numeroCandidato) {
        this.numeroCandidato = numeroCandidato;
    }

    public void setNomeEleitor(String nomeEleitor) {
        this.nomeEleitor = nomeEleitor;
    }
}

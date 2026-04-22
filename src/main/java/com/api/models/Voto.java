package com.api.models;

public class Voto {
    private int id;
    private int idEleitor;
    private int idCandidato;
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

    public int getIdCandidato() {
        return idCandidato;
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

    public void setIdCandidato(int idCandidato) {
        this.idCandidato = idCandidato;
    }

    public void setNomeEleitor(String nomeEleitor) {
        this.nomeEleitor = nomeEleitor;
    }
}

package com.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VotoBloco {

    private String cpfEleitor;
    @JsonIgnore
    private int numeroCandidato;
    private String votoHash;
    private String timestamp;

    public VotoBloco(String cpfEleitor, int numeroCandidato, String votoHash, String timestamp) {
        this.cpfEleitor = cpfEleitor;
        this.numeroCandidato = numeroCandidato;
        this.votoHash = votoHash;
        this.timestamp = timestamp;
    }

    public String getCpfEleitor() {
        return cpfEleitor;
    }

    public int getNumeroCandidato() {
        return numeroCandidato;
    }

    public String getVotoHash() {
        return votoHash;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setCpfEleitor(String cpfEleitor) {
        this.cpfEleitor = cpfEleitor;
    }

    public void setNumeroCandidato(int numeroCandidato) {
        this.numeroCandidato = numeroCandidato;
    }

    public void setVotoHash(String votoHash) {
        this.votoHash = votoHash;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
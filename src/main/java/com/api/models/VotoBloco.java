package com.api.models;

public class VotoBloco {

    private String cpfEleitor;
    private String candidatoHash;  // SHA-256 do numeroCandidato
    private String timestamp;

    public VotoBloco(String cpfEleitor, String candidatoHash, String timestamp) {
        this.cpfEleitor = cpfEleitor;
        this.candidatoHash = candidatoHash;
        this.timestamp = timestamp;
    }

    public String getCpfEleitor() {
        return cpfEleitor;
    }

    public String getCandidatoHash() {
        return candidatoHash;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setCpfEleitor(String cpfEleitor) {
        this.cpfEleitor = cpfEleitor;
    }

    public void setCandidatoHash(String candidatoHash) {
        this.candidatoHash = candidatoHash;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
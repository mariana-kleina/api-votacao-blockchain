package com.api.models;

import java.util.List;

public class Bloco {
    
    private int id;
    private String hashAnterior;
    private String hashAtual;
    private List<VotoBloco> votos;
    private String timestamp;
    private int totalVotos;
    
    public Bloco(int id, String hashAnterior, String hashAtual, List<VotoBloco> votos, String timestamp, int totalVotos) {
        this.id = id;
        this.hashAnterior = hashAnterior;
        this.hashAtual = hashAtual;
        this.votos = votos;
        this.timestamp = timestamp;
        this.totalVotos = totalVotos;
    }

    public int getId() {
        return id;
    }

    public String getHashAnterior() {
        return hashAnterior;
    }

    public String getHashAtual() {
        return hashAtual;
    }

    public List<VotoBloco> getVotos() {
        return votos;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getTotalVotos() {
        return totalVotos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHashAnterior(String hashAnterior) {
        this.hashAnterior = hashAnterior;
    }

    public void setHashAtual(String hashAtual) {
        this.hashAtual = hashAtual;
    }

    public void setVotos(List<VotoBloco> votos) {
        this.votos = votos;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTotalVotos(int totalVotos) {
        this.totalVotos = totalVotos;
    }
}

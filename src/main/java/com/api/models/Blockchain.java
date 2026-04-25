package com.api.models;

import java.util.List;

public class Blockchain {

    private List<Bloco> blocos;

    public Blockchain(List<Bloco> blocos) {
        this.blocos = blocos;
    }

    public List<Bloco> getBlocos() {
        return blocos;
    }

    public void setBlocos(List<Bloco> blocos) {
        this.blocos = blocos;
    }
}
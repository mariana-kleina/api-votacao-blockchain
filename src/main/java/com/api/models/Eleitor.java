package com.api.models;

public class Eleitor {
    private int id;
    private String nome;
    private String cpf;
    private int idade;
    private String cep;
    private String cidade;

    public Eleitor() {}

    public Eleitor(int id, String nome, String cpf, int idade, String cep, String cidade) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.cep = cep;
        this.cidade = cidade;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
}
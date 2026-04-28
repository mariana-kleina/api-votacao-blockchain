package com.api.dto;

public class ApiResponse {

    private boolean sucesso;
    private String mensagem;
    private Object dados;

    public ApiResponse(boolean sucesso, String mensagem, Object dados) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.dados = dados;
    }

    public boolean isSucesso() { return sucesso; }
    public String getMensagem() { return mensagem; }
    public Object getDados() { return dados; }
}
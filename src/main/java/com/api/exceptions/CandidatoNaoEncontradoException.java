package com.api.exceptions;

public class CandidatoNaoEncontradoException extends ApiException {
    public CandidatoNaoEncontradoException() {
        super("Candidato não encontrado");
    }
}
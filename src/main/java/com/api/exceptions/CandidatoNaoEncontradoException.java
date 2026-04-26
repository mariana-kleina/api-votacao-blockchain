package com.api.exceptions;

public class CandidatoNaoEncontradoException extends ResourceNotFoundException {
    public CandidatoNaoEncontradoException() {
        super("Candidato não encontrado");
    }
}
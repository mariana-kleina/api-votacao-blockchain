package com.api.exceptions;

public class NumeroCandidatoExistenteException extends RuntimeException {
    public NumeroCandidatoExistenteException() {
        super("Já existe um candidato com esse número!");
    }
}
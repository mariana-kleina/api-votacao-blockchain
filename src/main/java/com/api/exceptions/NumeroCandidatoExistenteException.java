package com.api.exceptions;

public class NumeroCandidatoExistenteException extends ApiException {
    public NumeroCandidatoExistenteException() {
        super("Já existe um candidato com esse número");
    }
}
package com.api.exceptions;

public class NumeroCandidatoExistenteException extends ValidationException {
    public NumeroCandidatoExistenteException() {
        super("Número de candidato já existe");
    }
}
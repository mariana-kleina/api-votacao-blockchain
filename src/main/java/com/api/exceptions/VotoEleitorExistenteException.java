package com.api.exceptions;

public class VotoEleitorExistenteException extends ApiException {
    public VotoEleitorExistenteException() {
        super("Já existe um voto desse eleitor");
    }
}
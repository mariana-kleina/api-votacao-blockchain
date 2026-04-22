package com.api.exceptions;

public class VotoEleitorExistenteException extends RuntimeException {
    public VotoEleitorExistenteException() {
        super("Já existe um voto desse eleitor!");
    }
}

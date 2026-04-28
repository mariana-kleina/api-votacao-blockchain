package com.api.exceptions;

public class VotoEleitorExistenteException extends ValidationException {
    public VotoEleitorExistenteException() {
        super("Eleitor já votou");
    }
}
package com.api.exceptions;

public class CpfDuplicadoException extends RuntimeException {
    public CpfDuplicadoException() {
        super("CPF já cadastrado");
    }
}
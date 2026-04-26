package com.api.util;

import java.io.IOException;

import com.api.exceptions.CpfDuplicadoException;
import com.api.exceptions.DatabaseException;
import com.api.exceptions.NumeroCandidatoExistenteException;
import com.api.exceptions.ResourceNotFoundException;
import com.api.exceptions.ValidationException;
import com.api.exceptions.VotoEleitorExistenteException;
import com.sun.net.httpserver.HttpExchange;

public class ExceptionHandlerUtil {

    public static void handle(HttpExchange exchange, Exception e) throws IOException {

        if (e instanceof ValidationException) {
            ResponseUtil.erro(exchange, 400, e.getMessage());

        } else if (e instanceof ResourceNotFoundException) {
            ResponseUtil.erro(exchange, 404, e.getMessage());

        } else if (e instanceof NumeroCandidatoExistenteException) {
            ResponseUtil.erro(exchange, 409, e.getMessage());

        } else if (e instanceof VotoEleitorExistenteException) {
            ResponseUtil.erro(exchange, 409, e.getMessage());

        } else if (e instanceof CpfDuplicadoException) {
            ResponseUtil.erro(exchange, 409, e.getMessage());

        } else if (e instanceof DatabaseException) {
            ResponseUtil.erro(exchange, 500, e.getMessage());

        } else if (e instanceof NumberFormatException) {
            ResponseUtil.erro(exchange, 400, "Formato inválido");

        } else if (e instanceof com.fasterxml.jackson.core.JsonProcessingException) {
            ResponseUtil.erro(exchange, 400, "JSON inválido");

        } else {
            e.printStackTrace();
            ResponseUtil.erro(exchange, 500, "Erro interno inesperado");
        }
    }
}
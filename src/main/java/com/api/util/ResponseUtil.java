package com.api.util;

import java.io.IOException;

import com.api.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

public class ResponseUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void sucesso(HttpExchange exchange, int status, String mensagem, Object dados) throws IOException {
        enviar(exchange, status, new ApiResponse(true, mensagem, dados));
    }

    public static void erro(HttpExchange exchange, int status, String mensagem) throws IOException {
        enviar(exchange, status, new ApiResponse(false, mensagem, null));
    }

    private static void enviar(HttpExchange exchange, int status, ApiResponse response) throws IOException {
        String json = mapper.writeValueAsString(response);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
    }
}
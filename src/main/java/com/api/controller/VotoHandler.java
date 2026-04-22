package com.api.controller;

import java.io.IOException;

import com.api.exceptions.VotoEleitorExistenteException;
import com.api.models.Voto;
import com.api.service.VotoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class VotoHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();

        if (metodo.equals("GET")) {
            listarVotos(exchange);
        } else if (metodo.equals("POST")) {
            cadastrarVoto(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void listarVotos(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        VotoService service = new VotoService();
        String json;

        try {
            json = mapper.writeValueAsString(service.listarVotos());
            enviarResposta(exchange, json);
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1);
            return;
        }
    }

    private void cadastrarVoto(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Voto voto = mapper.readValue(exchange.getRequestBody(), Voto.class);
        String json = mapper.writeValueAsString(voto);
        VotoService service = new VotoService();
        try {
            service.cadastrarVoto(voto);
            json = mapper.writeValueAsString(voto);
            exchange.sendResponseHeaders(201, json.getBytes().length);
            exchange.getResponseBody().write(json.getBytes());
        } catch (VotoEleitorExistenteException e) {
            exchange.sendResponseHeaders(409, -1);
        }
    }

    private void enviarResposta(HttpExchange exchange, String resposta) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resposta.getBytes().length);
        exchange.getResponseBody().write(resposta.getBytes());
        exchange.getResponseBody().close();
    }
}

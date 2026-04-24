package com.api.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.api.models.Candidato;
import com.api.repository.CandidatoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CandidatoHandler implements HttpHandler {
    private final CandidatoRepository repository = new CandidatoRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        int statusCode = 200;

        try {
            if ("POST".equals(method)) {
                response = cadastrar(lerCorpo(exchange));
                statusCode = 201;
            } else if ("GET".equals(method)) {
                String[] partes = path.split("/");
                if (partes.length > 2) {
                    response = mapper.writeValueAsString(repository.buscarPorId(Integer.parseInt(partes[2])));
                } else {
                    response = mapper.writeValueAsString(repository.buscarTodos());
                }
            } else if ("DELETE".equals(method)) {
                String[] partes = path.split("/");
                if (partes.length > 2) {
                    repository.deletar(Integer.parseInt(partes[2]));
                    response = "Candidato removido.";
                }
            }
        } catch (Exception e) {
            response = "Erro: " + e.getMessage();
            statusCode = 400;
        }
        enviarResposta(exchange, response, statusCode);
    }

    private String cadastrar(String json) throws Exception {
        Candidato c = mapper.readValue(json, Candidato.class);
        repository.salvar(c);
        return "Sucesso: Candidato cadastrado.";
    }

    private String lerCorpo(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private void enviarResposta(HttpExchange exchange, String response, int statusCode) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
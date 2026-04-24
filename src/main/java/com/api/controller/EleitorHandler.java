package com.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.api.exceptions.IdadeInvalidaException;
import com.api.models.Eleitor;
import com.api.repository.EleitorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class EleitorHandler implements HttpHandler {
    private final EleitorRepository repository = new EleitorRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";
        int statusCode = 200;

        try {
            if ("POST".equals(method)) {
                response = cadastrar(lerCorpo(exchange));
                statusCode = 201;
            } else if ("GET".equals(method)) {
                response = listar();
            } else if ("DELETE".equals(method)) {
                String path = exchange.getRequestURI().getPath();
                String[] partes = path.split("/");
                if (partes.length > 2) {
                    repository.deletar(Integer.parseInt(partes[2]));
                    response = "Eleitor removido com sucesso.";
                } else {
                    response = "ID necessário.";
                    statusCode = 400;
                }
            }
        } catch (IdadeInvalidaException e) {
            response = "Erro: " + e.getMessage();
            statusCode = 400;
        } catch (Exception e) {
            response = "Erro interno: " + e.getMessage();
            statusCode = 500;
        }

        enviarResposta(exchange, response, statusCode);
    }

    private String cadastrar(String json) throws Exception {
        Eleitor novoEleitor = mapper.readValue(json, Eleitor.class);
        if (novoEleitor.getIdade() < 16) {
            throw new IdadeInvalidaException("Eleitor inapto: idade mínima é 16 anos.");
        }
        repository.salvar(novoEleitor);
        return "Sucesso: Eleitor cadastrado.";
    }

    private String listar() throws Exception {
        return mapper.writeValueAsString(repository.buscarTodos());
    }

    private String lerCorpo(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private void enviarResposta(HttpExchange exchange, String response, int statusCode) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
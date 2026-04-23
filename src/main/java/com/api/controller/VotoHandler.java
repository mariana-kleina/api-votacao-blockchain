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
        String path = exchange.getRequestURI().getPath();

        String[] partes = path.split("/");

        try {
            if (metodo.equals("GET")) {
                if (partes.length == 3) {
                    int id = Integer.parseInt(partes[2]);
                    buscarPorId(exchange, id);
                } else {
                    listarVotos(exchange);
                }

            } else if (metodo.equals("POST")) {
                cadastrarVoto(exchange);

            } else if (metodo.equals("DELETE") && partes.length == 3) {
                int id = Integer.parseInt(partes[2]);
                deletarVoto(exchange, id);

            } else {
                exchange.sendResponseHeaders(405, -1);
            }

        } catch (Exception e) {
            exchange.sendResponseHeaders(400, -1);
        }
    }

    private void listarVotos(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        VotoService service = new VotoService();
        String json;

        try {
            json = mapper.writeValueAsString(service.listarVotos());
            enviarResposta(exchange, json, 200);
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1);
            return;
        }
    }

    private void cadastrarVoto(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        VotoService service = new VotoService();

        try {
            Voto voto = mapper.readValue(exchange.getRequestBody(), Voto.class);

            service.cadastrarVoto(voto);

            String resposta = "{\"mensagem\": \"Sucesso: Voto cadastrado.\"}";
            enviarResposta(exchange, resposta, 201);

        } catch (VotoEleitorExistenteException e) {
            enviarResposta(exchange, "{\"erro\": \"" + e.getMessage() + "\"}", 409);

        } catch (com.api.exceptions.CandidatoNaoEncontradoException e) {
            enviarResposta(exchange, "{\"erro\": \"" + e.getMessage() + "\"}", 404);

        } catch (IllegalArgumentException e) {
            enviarResposta(exchange, "{\"erro\": \"" + e.getMessage() + "\"}", 400);

        } catch (Exception e) {
            enviarResposta(exchange, "{\"erro\": \"Erro interno\"}", 500);
        }
    }

    private void buscarPorId(HttpExchange exchange, int id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        VotoService service = new VotoService();

        Voto voto = service.buscarPorId(id);

        if (voto == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        String json = mapper.writeValueAsString(voto);
        enviarResposta(exchange, json, 200);
    }

    private void deletarVoto(HttpExchange exchange, int id) throws IOException {
        VotoService service = new VotoService();

        boolean removido = service.deletar(id);

        if (removido) {
            exchange.sendResponseHeaders(204, -1);
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }

    private void enviarResposta(HttpExchange exchange, String resposta, int status) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, resposta.getBytes().length);
        exchange.getResponseBody().write(resposta.getBytes());
        exchange.getResponseBody().close();
    }
}

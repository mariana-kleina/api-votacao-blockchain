package com.api.controller;

import java.io.IOException;

import com.api.models.VotoBloco;
import com.api.service.BlockchainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class BlockchainHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] partes = path.split("/");

        try {
            if (metodo.equals("GET")) {

                // GET /blockchain
                if (partes.length == 2) {
                    listarBlocos(exchange);

                // GET /blockchain/total
                } else if (partes.length == 3 && partes[2].equals("total")) {
                    totalDeVotos(exchange);

                // GET /blockchain/eleitor/{cpf}
                } else if (partes.length == 4 && partes[2].equals("eleitor")) {
                    String cpf = partes[3];
                    verificarEleitor(exchange, cpf);

                // GET /blockchain/candidato/{numero}
                } else if (partes.length == 4 && partes[2].equals("candidato")) {
                    int numero = Integer.parseInt(partes[3]);
                    votosPorCandidato(exchange, numero);

                } else {
                    exchange.sendResponseHeaders(404, -1);
                }

            } else {
                exchange.sendResponseHeaders(405, -1);
            }

        } catch (Exception e) {
            exchange.sendResponseHeaders(400, -1);
        }
    }

    private void listarBlocos(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BlockchainService service = new BlockchainService();

        try {
            String json = mapper.writeValueAsString(service.getBlockchain());
            enviarResposta(exchange, json, 200);
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private void verificarEleitor(HttpExchange exchange, String cpf) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BlockchainService service = new BlockchainService();

        try {
            VotoBloco voto = service.verificarEleitor(cpf);

            if (voto == null) {
                enviarResposta(exchange, "{\"mensagem\": \"O eleitor não realizou o voto\"}", 200);
            } else {
                String json = mapper.writeValueAsString(voto);
                enviarResposta(exchange, json, 200);
            }

        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private void votosPorCandidato(HttpExchange exchange, int numero) throws IOException {
        BlockchainService service = new BlockchainService();

        try {
            int total = service.votosPorCandidato(numero);
            enviarResposta(exchange, "{\"totalVotos\": " + total + "}", 200);
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private void totalDeVotos(HttpExchange exchange) throws IOException {
        BlockchainService service = new BlockchainService();

        try {
            int total = service.totalDeVotos();
            enviarResposta(exchange, "{\"totalVotos\": " + total + "}", 200);
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private void enviarResposta(HttpExchange exchange, String resposta, int status) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, resposta.getBytes().length);
        exchange.getResponseBody().write(resposta.getBytes());
        exchange.getResponseBody().close();
    }
}
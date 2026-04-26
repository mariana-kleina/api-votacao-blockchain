package com.api.controller;

import java.io.IOException;

import com.api.exceptions.ApiException;
import com.api.models.Candidato;
import com.api.repository.CandidatoRepository;
import com.api.util.ResponseUtil;
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

        try {

            if ("POST".equals(method)) {

                Candidato candidato = mapper.readValue(exchange.getRequestBody(), Candidato.class);
                repository.salvar(candidato);

                ResponseUtil.sucesso(exchange, 201, "Candidato cadastrado", null);

            } else if ("GET".equals(method)) {

                String[] partes = path.split("/");

                if (partes.length > 2) {
                    int id = Integer.parseInt(partes[2]);
                    ResponseUtil.sucesso(exchange, 200, "Candidato encontrado", repository.buscarPorId(id));
                } else {
                    ResponseUtil.sucesso(exchange, 200, "Lista de candidatos", repository.buscarTodos());
                }

            } else if ("DELETE".equals(method)) {

                String[] partes = path.split("/");
                int id = Integer.parseInt(partes[2]);

                repository.deletar(id);

                ResponseUtil.sucesso(exchange, 200, "Candidato removido", null);

            } else {
                ResponseUtil.erro(exchange, 405, "Método não permitido");
            }

        } catch (ApiException e) {
            ResponseUtil.erro(exchange, 400, e.getMessage());

        } catch (Exception e) {
            ResponseUtil.erro(exchange, 500, "Erro interno");
        }
    }
}
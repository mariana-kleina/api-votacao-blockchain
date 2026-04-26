package com.api.controller;

import java.io.IOException;

import com.api.exceptions.ApiException;
import com.api.exceptions.IdadeInvalidaException;
import com.api.models.Eleitor;
import com.api.repository.EleitorRepository;
import com.api.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class EleitorHandler implements HttpHandler {

    private final EleitorRepository repository = new EleitorRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {

            if ("POST".equals(method)) {
                Eleitor eleitor = mapper.readValue(exchange.getRequestBody(), Eleitor.class);

                if (eleitor.getIdade() < 16) {
                    throw new IdadeInvalidaException("Idade mínima é 16 anos");
                }

                repository.salvar(eleitor);

                ResponseUtil.sucesso(exchange, 201, "Eleitor cadastrado", null);

            } else if ("GET".equals(method)) {

                ResponseUtil.sucesso(exchange, 200, "Lista de eleitores", repository.buscarTodos());

            } else if ("PUT".equals(method)) {

                String[] partes = path.split("/");
                int id = Integer.parseInt(partes[2]);

                Eleitor eleitor = mapper.readValue(exchange.getRequestBody(), Eleitor.class);

                if (eleitor.getIdade() < 16) {
                    throw new IdadeInvalidaException("Idade mínima é 16 anos");
                }

                repository.atualizar(id, eleitor);

                ResponseUtil.sucesso(exchange, 200, "Eleitor atualizado", null);

            } else if ("DELETE".equals(method)) {

                String[] partes = path.split("/");
                int id = Integer.parseInt(partes[2]);

                repository.deletar(id);

                ResponseUtil.sucesso(exchange, 200, "Eleitor removido", null);

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
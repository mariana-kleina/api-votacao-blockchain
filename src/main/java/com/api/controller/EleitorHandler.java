package com.api.controller;

import java.io.IOException;

import com.api.exceptions.ValidationException;
import com.api.models.Eleitor;
import com.api.repository.EleitorRepository;
import com.api.util.ExceptionHandlerUtil;
import com.api.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class EleitorHandler implements HttpHandler {

    private final EleitorRepository repository = new EleitorRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("POST".equals(method)) {

                Eleitor e = mapper.readValue(exchange.getRequestBody(), Eleitor.class);
                validar(e);

                repository.salvar(e);
                ResponseUtil.sucesso(exchange, 201, "Eleitor cadastrado", null);

            } else if ("GET".equals(method)) {

                ResponseUtil.sucesso(exchange, 200, "Lista de eleitores", repository.buscarTodos());

            } else if ("PUT".equals(method)) {

                int id = Integer.parseInt(path.split("/")[2]);
                Eleitor e = mapper.readValue(exchange.getRequestBody(), Eleitor.class);

                validar(e);
                repository.atualizar(id, e);

                ResponseUtil.sucesso(exchange, 200, "Eleitor atualizado", null);

            } else if ("DELETE".equals(method)) {

                int id = Integer.parseInt(path.split("/")[2]);
                repository.deletar(id);

                ResponseUtil.sucesso(exchange, 200, "Eleitor removido", null);

            } else {
                ResponseUtil.erro(exchange, 405, "Método não permitido");
            }

        } catch (Exception e) {
            ExceptionHandlerUtil.handle(exchange, e);
        }
    }

    private void validar(Eleitor e) {

        if (e.getNome() == null || e.getNome().isBlank())
            throw new ValidationException("Nome é obrigatório");

        if (e.getCpf() == null || !e.getCpf().matches("\\d{11}"))
            throw new ValidationException("CPF inválido");

        if (e.getIdade() < 16)
            throw new ValidationException("Idade mínima é 16 anos");
    }
}
package com.api.controller;

import java.io.IOException;

import com.api.models.Candidato;
import com.api.service.CandidatoService;
import com.api.util.ExceptionHandlerUtil;
import com.api.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CandidatoHandler implements HttpHandler {

    private final CandidatoService service = new CandidatoService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("POST".equals(method)) {

                Candidato c = mapper.readValue(exchange.getRequestBody(), Candidato.class);
                service.cadastrar(c);

                ResponseUtil.sucesso(exchange, 201, "Candidato cadastrado", null);

            } else if ("GET".equals(method)) {

                String[] partes = path.split("/");

                if (partes.length > 2) {
                    int id = Integer.parseInt(partes[2]);
                    ResponseUtil.sucesso(exchange, 200, "Candidato encontrado", service.buscar(id));
                } else {
                    ResponseUtil.sucesso(exchange, 200, "Lista", service.listar());
                }

            } else if ("DELETE".equals(method)) {

                int id = Integer.parseInt(path.split("/")[2]);
                service.deletar(id);

                ResponseUtil.sucesso(exchange, 200, "Removido", null);

            } else {
                ResponseUtil.erro(exchange, 405, "Método não permitido");
            }

        } catch (Exception e) {
            ExceptionHandlerUtil.handle(exchange, e);
        }
    }
}
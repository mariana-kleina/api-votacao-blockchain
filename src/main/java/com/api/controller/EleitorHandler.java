package com.api.controller;

import java.io.IOException;

import com.api.models.Eleitor;
import com.api.service.EleitorService;
import com.api.util.ExceptionHandlerUtil;
import com.api.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class EleitorHandler implements HttpHandler {

    private final EleitorService service = new EleitorService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] partes = path.split("/");

            if ("POST".equals(method)) {

                Eleitor eleitor = mapper.readValue(exchange.getRequestBody(), Eleitor.class);
                service.cadastrar(eleitor);
                ResponseUtil.sucesso(exchange, 201, "Eleitor cadastrado com sucesso", null);

            } else if ("GET".equals(method)) {

                if (partes.length > 2) {
                    int id = Integer.parseInt(partes[2]);
                    Eleitor eleitor = service.buscarPorId(id);
                    ResponseUtil.sucesso(exchange, 200, "Eleitor encontrado", eleitor);
                } else {
                    ResponseUtil.sucesso(exchange, 200, "Lista de eleitores", service.listar());
                }

            } else if ("PUT".equals(method)) {

                if (partes.length > 2) {
                    int id = Integer.parseInt(partes[2]);
                    Eleitor eleitor = mapper.readValue(exchange.getRequestBody(), Eleitor.class);
                    service.atualizar(id, eleitor);
                    ResponseUtil.sucesso(exchange, 200, "Eleitor atualizado com sucesso", null);
                } else {
                    ResponseUtil.erro(exchange, 400, "ID necessário");
                }

            } else if ("DELETE".equals(method)) {

                if (partes.length > 2) {
                    int id = Integer.parseInt(partes[2]);
                    service.deletar(id);
                    ResponseUtil.sucesso(exchange, 200, "Eleitor removido com sucesso", null);
                } else {
                    ResponseUtil.erro(exchange, 400, "ID necessário");
                }

            } else {
                ResponseUtil.erro(exchange, 405, "Método não permitido");
            }

        } catch (Exception e) {
            ExceptionHandlerUtil.handle(exchange, e);
        }
    }
}

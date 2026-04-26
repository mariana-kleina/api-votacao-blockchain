package com.api.controller;

import java.io.IOException;

import com.api.exceptions.ApiException;
import com.api.models.Voto;
import com.api.service.VotoService;
import com.api.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VotoHandler implements HttpHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final VotoService service = new VotoService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] partes = path.split("/");

        try {

            if ("GET".equals(metodo)) {

                if (partes.length == 3) {
                    int id = Integer.parseInt(partes[2]);
                    Voto voto = service.buscarPorId(id);

                    if (voto == null) {
                        ResponseUtil.erro(exchange, 404, "Voto não encontrado");
                    } else {
                        ResponseUtil.sucesso(exchange, 200, "Voto encontrado", voto);
                    }

                } else {
                    ResponseUtil.sucesso(exchange, 200, "Lista de votos", service.listarVotos());
                }

            } else if ("POST".equals(metodo)) {

                Voto voto = mapper.readValue(exchange.getRequestBody(), Voto.class);
                service.cadastrarVoto(voto);

                ResponseUtil.sucesso(exchange, 201, "Voto cadastrado", null);

            } else if ("DELETE".equals(metodo) && partes.length == 3) {

                int id = Integer.parseInt(partes[2]);
                boolean removido = service.deletar(id);

                if (removido) {
                    ResponseUtil.sucesso(exchange, 200, "Voto removido", null);
                } else {
                    ResponseUtil.erro(exchange, 404, "Voto não encontrado");
                }

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
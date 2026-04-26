package com.api.controller;

import java.io.IOException;

import com.api.exceptions.ValidationException;
import com.api.models.Voto;
import com.api.service.VotoService;
import com.api.util.ExceptionHandlerUtil;
import com.api.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VotoHandler implements HttpHandler {

    private final VotoService service = new VotoService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            String method = exchange.getRequestMethod();

            if ("POST".equals(method)) {

                Voto voto = mapper.readValue(exchange.getRequestBody(), Voto.class);

                if (voto.getIdEleitor() <= 0)
                    throw new ValidationException("ID do eleitor inválido");

                if (voto.getNumeroCandidato() <= 0)
                    throw new ValidationException("Número do candidato inválido");

                service.cadastrarVoto(voto);

                ResponseUtil.sucesso(exchange, 201, "Voto registrado", null);

            } else if ("GET".equals(method)) {

                var lista = service.listarVotos();

                if (lista.isEmpty()) {
                    ResponseUtil.sucesso(exchange, 200, "Nenhum voto encontrado", lista);
                } else {
                    ResponseUtil.sucesso(exchange, 200, "Lista de votos", lista);
                }
            } else {
                ResponseUtil.erro(exchange, 405, "Método não permitido");
            }

        } catch (Exception e) {
            ExceptionHandlerUtil.handle(exchange, e);
        }
    }
}
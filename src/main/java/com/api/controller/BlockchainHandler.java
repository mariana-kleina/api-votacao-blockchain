package com.api.controller;

import java.io.IOException;

import com.api.exceptions.ApiException;
import com.api.models.VotoBloco;
import com.api.service.BlockchainService;
import com.api.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class BlockchainHandler implements HttpHandler {

    private final BlockchainService service = new BlockchainService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] partes = path.split("/");

        try {

            if ("GET".equals(metodo)) {

                if (partes.length == 2) {

                    ResponseUtil.sucesso(exchange, 200, "Blockchain completa", service.getBlockchain());

                } else if (partes.length == 3 && partes[2].equals("total")) {

                    ResponseUtil.sucesso(exchange, 200, "Total de votos", service.totalDeVotos());

                } else if (partes.length == 4 && partes[2].equals("eleitor")) {

                    String cpf = partes[3];
                    VotoBloco voto = service.verificarEleitor(cpf);

                    if (voto == null) {
                        ResponseUtil.erro(exchange, 404, "Eleitor não votou");
                    } else {
                        ResponseUtil.sucesso(exchange, 200, "Voto encontrado", voto);
                    }

                } else if (partes.length == 4 && partes[2].equals("candidato")) {

                    int numero = Integer.parseInt(partes[3]);
                    int total = service.votosPorCandidato(numero);

                    ResponseUtil.sucesso(exchange, 200, "Total por candidato", total);

                } else {
                    ResponseUtil.erro(exchange, 404, "Rota não encontrada");
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
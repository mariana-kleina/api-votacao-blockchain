package com.api.controller;

import java.io.IOException;

import com.api.models.VotoBloco;
import com.api.service.BlockchainService;
import com.api.util.ExceptionHandlerUtil;
import com.api.util.ResponseUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class BlockchainHandler implements HttpHandler {

    private final BlockchainService service = new BlockchainService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            String metodo = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] partes = path.split("/");

            if (!"GET".equals(metodo)) {
                ResponseUtil.erro(exchange, 405, "Método não permitido");
                return;
            }

            // ✅ /blockchain
            if (partes.length == 2) {

                var blockchain = service.getBlockchain();

                if (blockchain.getBlocos().isEmpty()) {

                    int votos = service.totalDeVotos();

                    ResponseUtil.sucesso(
                        exchange,
                        200,
                        "Ainda não há blocos fechados. São necessários 3 votos para formar um bloco. Votos atuais: " + votos,
                        null
                    );
                    return;
                }

                ResponseUtil.sucesso(exchange, 200, "Blockchain completa", blockchain);
            }

            // ✅ /blockchain/total
            else if (partes.length == 3 && partes[2].equals("total")) {

                int total = service.totalDeVotos();

                ResponseUtil.sucesso(exchange, 200, "Total de votos", total);
            }

            // ✅ /blockchain/candidato/{numero}
            else if (partes.length == 4 && partes[2].equals("candidato")) {

                int numero = Integer.parseInt(partes[3]);

                int total = service.votosPorCandidato(numero);

                ResponseUtil.sucesso(exchange, 200, "Votos por candidato", total);
            }

            // ✅ /blockchain/eleitor/{cpf}
            else if (partes.length == 4 && partes[2].equals("eleitor")) {

                String cpf = partes[3];

                if (!cpf.matches("\\d{11}")) {
                    ResponseUtil.erro(exchange, 400, "CPF inválido");
                    return;
                }

                VotoBloco voto = service.verificarEleitor(cpf);

                if (voto == null) {
                    ResponseUtil.sucesso(exchange, 200, "Eleitor não votou", null);
                } else {
                    ResponseUtil.sucesso(exchange, 200, "Voto encontrado", voto);
                }
            }

            else {
                ResponseUtil.erro(exchange, 404, "Rota não encontrada");
            }

        } catch (Exception e) {
            ExceptionHandlerUtil.handle(exchange, e);
        }
    }
}
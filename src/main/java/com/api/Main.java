package com.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.api.config.ConnectionFactory;
import com.api.controller.CandidatoController;
import com.api.controller.EleitorController;
import com.api.controller.VotoHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class Main {
    public static void main(String[] args) throws Exception {
        ConnectionFactory.inicializarBanco();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/eleitor", exchange -> {
            EleitorController controller = new EleitorController();
            String response;
            int statusCode = 200;
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();

                if ("POST".equals(method)) {
                    response = controller.cadastrar(lerCorpo(exchange));
                } else if ("GET".equals(method)) {
                    String[] partes = path.split("/");
                    if (partes.length > 2) {
                        response = controller.listar();
                    } else {
                        response = controller.listar();
                    }
                } else if ("DELETE".equals(method)) {
                    String[] partes = path.split("/");
                    if (partes.length > 2) {
                        controller.deletar(Integer.parseInt(partes[2]));
                        response = "Eleitor removido com sucesso.";
                    } else {
                        response = "ID necessário para deleção.";
                        statusCode = 400;
                    }
                } else {
                    response = "Metodo nao suportado";
                    statusCode = 405;
                }
            } catch (Exception e) {
                response = "Erro: " + e.getMessage();
                statusCode = 400;
            }
            enviarResposta(exchange, response, statusCode);
        });

        server.createContext("/candidato", exchange -> {
            CandidatoController controller = new CandidatoController();
            String response;
            int statusCode = 200;
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();

                if ("POST".equals(method)) {
                    response = controller.cadastrar(lerCorpo(exchange));
                } else if ("GET".equals(method)) {
                    String[] partes = path.split("/");
                    if (partes.length > 2) {
                        response = controller.buscarPorId(Integer.parseInt(partes[2]));
                    } else {
                        response = controller.listar();
                    }
                } else if ("DELETE".equals(method)) {
                    String[] partes = path.split("/");
                    if (partes.length > 2) {
                        response = controller.deletar(Integer.parseInt(partes[2]));
                    } else {
                        response = "ID necessário.";
                        statusCode = 400;
                    }
                } else {
                    response = "Metodo nao suportado";
                    statusCode = 405;
                }
            } catch (Exception e) {
                response = "Erro: " + e.getMessage();
                statusCode = 400;
            }
            enviarResposta(exchange, response, statusCode);
        });

        server.createContext("/voto", new VotoHandler());

        server.setExecutor(null);
        System.out.println("Servidor rodando em: http://localhost:8080");
        server.start();
    }

    private static String lerCorpo(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static void enviarResposta(HttpExchange exchange, String response, int statusCode) {
        try {
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } catch (IOException e) {
            System.err.println("Erro ao enviar resposta: " + e.getMessage());
        }
    }
}
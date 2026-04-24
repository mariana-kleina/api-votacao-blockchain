package com.api;

import java.net.InetSocketAddress;

import com.api.config.ConnectionFactory;
import com.api.controller.CandidatoHandler;
import com.api.controller.EleitorHandler;
import com.api.controller.VotoHandler;
import com.sun.net.httpserver.HttpServer;

public class Main {
    public static void main(String[] args) throws Exception {

        ConnectionFactory.inicializarBanco();


        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);


        server.createContext("/eleitor", new EleitorHandler());
        server.createContext("/candidato", new CandidatoHandler());
        server.createContext("/voto", new VotoHandler());

        server.setExecutor(null);
        System.out.println("Servidor rodando em: http://localhost:8080");
        server.start();
    }
}
package com.api;

import java.net.InetSocketAddress;

import com.api.config.ConnectionFactory;
import com.sun.net.httpserver.HttpServer;

public class Main {
    public static void main(String[] args) throws Exception {

        ConnectionFactory.inicializarBanco();


        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        System.out.println("Servidor rodando na porta 8080...");
        server.start();
    }
}
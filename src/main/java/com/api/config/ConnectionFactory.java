package com.api.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {

    private static final String URL = "jdbc:h2:~/blockchain_db;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void inicializarBanco() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS eleitores (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nome VARCHAR(255), " +
                    "cpf VARCHAR(14), " +
                    "idade INT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS candidatos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nome VARCHAR(255), " +
                    "partido VARCHAR(100), " +
                    "categoria VARCHAR(100), " +
                    "numero INT UNIQUE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS votos(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "idEleitor INT UNIQUE, " +
                    "numeroCandidato INT, " +
                    "FOREIGN KEY (idEleitor) REFERENCES eleitores(id), " +
                    "FOREIGN KEY (numeroCandidato) REFERENCES candidatos(numero)" +
                    ")");

            System.out.println("Banco de dados pronto e tabelas criadas!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
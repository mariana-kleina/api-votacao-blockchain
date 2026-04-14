package com.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.api.config.ConnectionFactory;
import com.api.models.Eleitor;

public class EleitorRepository {
    public void salvar(Eleitor eleitor) {
        String sql = "INSERT INTO eleitores (nome, cpf, idade) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eleitor.getNome());
            stmt.setString(2, eleitor.getCpf());
            stmt.setInt(3, eleitor.getIdade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar eleitor: " + e.getMessage());
        }
    }

    public List<Eleitor> buscarTodos() {
        List<Eleitor> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM eleitores")) {
            while (rs.next()) {
                lista.add(new Eleitor(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"), rs.getInt("idade")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar eleitores.");
        }
        return lista;
    }
}
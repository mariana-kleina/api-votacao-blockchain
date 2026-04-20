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

        String sqlCheck = "SELECT COUNT(*) FROM eleitores WHERE cpf = ?";
        String sqlInsert = "INSERT INTO eleitores (nome, cpf, idade) VALUES (?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection()) {
            try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
                pstmtCheck.setString(1, eleitor.getCpf());
                ResultSet rs = pstmtCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new RuntimeException("CPF já cadastrado!");
                }
            }

            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                pstmtInsert.setString(1, eleitor.getNome());
                pstmtInsert.setString(2, eleitor.getCpf());
                pstmtInsert.setInt(3, eleitor.getIdade());
                pstmtInsert.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro no banco: " + e.getMessage());
        }
    }

    public List<Eleitor> buscarTodos() {
        List<Eleitor> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM eleitores")) {
            while (rs.next()) {
                lista.add(mapearEleitor(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return lista;
    }

    public Eleitor buscarPorId(int id) {
        String sql = "SELECT * FROM eleitores WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapearEleitor(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public void deletar(int id) {
        String sql = "DELETE FROM eleitores WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }


    private Eleitor mapearEleitor(ResultSet rs) throws SQLException {
        return new Eleitor(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"), rs.getInt("idade"));
    }
}
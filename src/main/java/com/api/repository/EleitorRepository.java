package com.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.api.config.ConnectionFactory;
import com.api.exceptions.CpfDuplicadoException;
import com.api.exceptions.DatabaseException;
import com.api.exceptions.ResourceNotFoundException;
import com.api.models.Eleitor;

public class EleitorRepository {

    public void salvar(Eleitor eleitor) {

        String sqlCheck = "SELECT COUNT(*) FROM eleitores WHERE cpf = ?";
        String sqlInsert = "INSERT INTO eleitores (nome, cpf, idade) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {

            try (PreparedStatement check = conn.prepareStatement(sqlCheck)) {
                check.setString(1, eleitor.getCpf());
                ResultSet rs = check.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    throw new CpfDuplicadoException();
                }
            }

            try (PreparedStatement insert = conn.prepareStatement(sqlInsert)) {
                insert.setString(1, eleitor.getNome());
                insert.setString(2, eleitor.getCpf());
                insert.setInt(3, eleitor.getIdade());
                insert.executeUpdate();
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar eleitor");
        }
    }

    public List<Eleitor> buscarTodos() {
        List<Eleitor> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM eleitores")) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar eleitores");
        }

        return lista;
    }

    public void atualizar(int id, Eleitor eleitor) {

        String sql = "UPDATE eleitores SET nome=?, cpf=?, idade=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eleitor.getNome());
            stmt.setString(2, eleitor.getCpf());
            stmt.setInt(3, eleitor.getIdade());
            stmt.setInt(4, id);

            if (stmt.executeUpdate() == 0) {
                throw new ResourceNotFoundException("Eleitor não encontrado");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar eleitor");
        }
    }

    public void deletar(int id) {

        String sql = "DELETE FROM eleitores WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (stmt.executeUpdate() == 0) {
                throw new ResourceNotFoundException("Eleitor não encontrado");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar eleitor");
        }
    }

    private Eleitor mapear(ResultSet rs) throws SQLException {
        return new Eleitor(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("cpf"),
                rs.getInt("idade")
        );
    }

    public Eleitor buscarPorId(int id) {

        String sql = "SELECT * FROM eleitores WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Eleitor(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getInt("idade")
                );
            }

            return null;

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar eleitor por ID");
        }
    }
}
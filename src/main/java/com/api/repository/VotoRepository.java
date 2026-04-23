package com.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.api.config.ConnectionFactory;
import com.api.models.Voto;
import com.api.exceptions.VotoEleitorExistenteException;

public class VotoRepository {

    public boolean candidatoExiste(int numero) {
        String sql = "SELECT COUNT(*) FROM candidatos WHERE numero = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void salvar(Voto voto) {

        String verificaSql = "SELECT COUNT(*) FROM votos WHERE idEleitor = ?";
        String insertSql = "INSERT INTO votos (idEleitor, numeroCandidato) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {

            try (PreparedStatement verificaStmt = conn.prepareStatement(verificaSql)) {
                verificaStmt.setInt(1, voto.getIdEleitor());
                ResultSet rs = verificaStmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    throw new VotoEleitorExistenteException();
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setInt(1, voto.getIdEleitor());
                stmt.setInt(2, voto.getNumeroCandidato());
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar voto: " + e.getMessage());
        }
    }

    private Voto mapearVoto(ResultSet rs) throws SQLException {
        Voto voto = new Voto();
        voto.setId(rs.getInt("id"));
        voto.setIdEleitor(rs.getInt("idEleitor"));
        voto.setNomeEleitor(rs.getString("nomeEleitor"));
        voto.setNumeroCandidato(rs.getInt("numeroCandidato"));
        voto.setNomeCandidato(rs.getString("nomeCandidato"));
        return voto;
    }

    public List<Voto> buscarTodos() {
        List<Voto> lista = new ArrayList<>();

        String sql = """
                    SELECT
                        v.id,
                        v.idEleitor,
                        e.nome AS nomeEleitor,
                        v.numeroCandidato,
                        c.nome AS nomeCandidato
                    FROM votos v
                    JOIN eleitores e ON v.idEleitor = e.id
                    JOIN candidatos c ON v.numeroCandidato = c.numero
                    ORDER BY v.id
                """;

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearVoto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public Voto buscarPorId(int id) {
        String sql = """
                    SELECT
                        v.id,
                        v.idEleitor,
                        e.nome AS nomeEleitor,
                        v.numeroCandidato,
                        c.nome AS nomeCandidato
                    FROM votos v
                    JOIN eleitores e ON v.idEleitor = e.id
                    JOIN candidatos c ON v.numeroCandidato = c.numero
                    WHERE v.id = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearVoto(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM votos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();

            return linhas > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
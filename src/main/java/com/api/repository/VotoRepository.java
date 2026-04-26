package com.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.api.config.ConnectionFactory;
import com.api.exceptions.DatabaseException;
import com.api.exceptions.VotoEleitorExistenteException;
import com.api.models.Voto;

public class VotoRepository {

    public boolean candidatoExiste(int numero) {

        String sql = "SELECT COUNT(*) FROM candidatos WHERE numero = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar candidato");
        }
    }

    public void salvar(Voto voto) {

        String verificaSql = "SELECT COUNT(*) FROM votos WHERE idEleitor = ?";
        String insertSql = "INSERT INTO votos (idEleitor, numeroCandidato) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {

            try (PreparedStatement verifica = conn.prepareStatement(verificaSql)) {
                verifica.setInt(1, voto.getIdEleitor());
                ResultSet rs = verifica.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    throw new VotoEleitorExistenteException();
                }
            }

            try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                insert.setInt(1, voto.getIdEleitor());
                insert.setInt(2, voto.getNumeroCandidato());
                insert.executeUpdate();
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar voto");
        }
    }

    public List<Voto> buscarTodos() {

        List<Voto> lista = new ArrayList<>();

        String sql = """
            SELECT v.id, v.idEleitor, e.nome AS nomeEleitor,
                   v.numeroCandidato, c.nome AS nomeCandidato
            FROM votos v
            JOIN eleitores e ON v.idEleitor = e.id
            JOIN candidatos c ON v.numeroCandidato = c.numero
            ORDER BY v.id
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar votos");
        }

        return lista;
    }

    public Voto buscarPorId(int id) {

        String sql = """
            SELECT v.id, v.idEleitor, e.nome AS nomeEleitor,
                   v.numeroCandidato, c.nome AS nomeCandidato
            FROM votos v
            JOIN eleitores e ON v.idEleitor = e.id
            JOIN candidatos c ON v.numeroCandidato = c.numero
            WHERE v.id = ?
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return mapear(rs);

            return null;

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar voto");
        }
    }

    public boolean deletar(int id) {

        String sql = "DELETE FROM votos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar voto");
        }
    }

    private Voto mapear(ResultSet rs) throws SQLException {
        Voto v = new Voto();
        v.setId(rs.getInt("id"));
        v.setIdEleitor(rs.getInt("idEleitor"));
        v.setNomeEleitor(rs.getString("nomeEleitor"));
        v.setNumeroCandidato(rs.getInt("numeroCandidato"));
        v.setNomeCandidato(rs.getString("nomeCandidato"));
        return v;
    }
}
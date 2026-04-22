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
    public void salvar(Voto voto) {

        String verificaSql = "SELECT COUNT(*) FROM votos WHERE idEleitor = ?";
        String insertSql = "INSERT INTO votos (idEleitor, idCandidato) VALUES (?, ?)";

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
                stmt.setInt(2, voto.getIdCandidato());
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
        voto.setIdCandidato(rs.getInt("idCandidato"));
        voto.setNomeCandidato(rs.getString("nomeCandidato"));
        return voto;
    }

    public List<Voto> buscarTodos() {
        List<Voto> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement("""
                            SELECT
                                v.id,
                                v.idEleitor,
                                e.nome AS nomeEleitor,
                                v.idCandidato,
                                c.nome AS nomeCandidato
                            FROM votos v
                            JOIN eleitores e ON v.idEleitor = e.id
                            JOIN candidatos c ON v.idCandidato = c.id
                            ORDER BY v.id
                        """);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearVoto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }
}

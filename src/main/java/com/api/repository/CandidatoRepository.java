package com.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.api.config.ConnectionFactory;
import com.api.exceptions.CandidatoNaoEncontradoException;
import com.api.exceptions.NumeroCandidatoExistenteException;
import com.api.models.Candidato;

public class CandidatoRepository {

    public void salvar(Candidato candidato) {

        String verificaSql = "SELECT COUNT(*) FROM candidatos WHERE numero = ?";
        String insertSql = "INSERT INTO candidatos (nome, partido, categoria, numero) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {

          
            try (PreparedStatement verificaStmt = conn.prepareStatement(verificaSql)) {
                verificaStmt.setInt(1, candidato.getNumero());
                ResultSet rs = verificaStmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    throw new NumeroCandidatoExistenteException();
                }
            }

            
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setString(1, candidato.getNome());
                stmt.setString(2, candidato.getPartido());
                stmt.setString(3, candidato.getCategoria());
                stmt.setInt(4, candidato.getNumero());
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar candidato: " + e.getMessage());
        }
    }

   
    public List<Candidato> buscarTodos() {
        List<Candidato> lista = new ArrayList<>();
        String sql = "SELECT * FROM candidatos";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCandidato(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar candidatos: " + e.getMessage());
        }

        return lista;
    }


    public Candidato buscarPorId(int id) {
        String sql = "SELECT * FROM candidatos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCandidato(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar candidato por ID: " + e.getMessage());
        }

        throw new CandidatoNaoEncontradoException();
    }

    
    public void deletar(int id) {
        String sql = "DELETE FROM candidatos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();

            if (linhas == 0) {
                throw new CandidatoNaoEncontradoException();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar candidato: " + e.getMessage());
        }
    }

    
    public List<Candidato> buscarPorPartido(String partido) {
        String sql = "SELECT * FROM candidatos WHERE LOWER(partido) = LOWER(?)";
        return buscarComFiltro(sql, partido);
    }

    
    public List<Candidato> buscarPorCategoria(String categoria) {
        String sql = "SELECT * FROM candidatos WHERE LOWER(categoria) = LOWER(?)";
        return buscarComFiltro(sql, categoria);
    }

    
    public List<Candidato> classificar(String categoria, String partido) {
        List<Candidato> lista = new ArrayList<>();
        String sql = "SELECT * FROM candidatos WHERE LOWER(categoria) = LOWER(?) AND LOWER(partido) = LOWER(?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria);
            stmt.setString(2, partido);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCandidato(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao classificar candidatos: " + e.getMessage());
        }

        return lista;
    }

    
    private List<Candidato> buscarComFiltro(String sql, String valor) {
        List<Candidato> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCandidato(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar filtro: " + e.getMessage());
        }

        return lista;
    }

    private Candidato mapearCandidato(ResultSet rs) throws SQLException {
        return new Candidato(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("partido"),
            rs.getString("categoria"),
            rs.getInt("numero")
        );
    }
}
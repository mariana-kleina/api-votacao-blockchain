package com.api.repository;

import java.util.ArrayList;
import java.util.List;

import com.api.models.Candidato;
import com.api.exceptions.CandidatoNaoEncontradoException;
import com.api.exceptions.NumeroCandidatoExistenteException;

public class CandidatoRepository {

    private final List<Candidato> bancoSimulado = new ArrayList<>();
    private int contadorId = 1;

    public void salvar(Candidato candidato) {

    
        boolean numeroExiste = bancoSimulado.stream()
            .anyMatch(c -> c.getNumero() == candidato.getNumero());

        if (numeroExiste) {
            throw new NumeroCandidatoExistenteException();
        }

        candidato.setId(contadorId++);
        bancoSimulado.add(candidato);
    }

    public List<Candidato> buscarTodos() {
        return bancoSimulado;
    }

    public Candidato buscarPorId(int id) {
        return bancoSimulado.stream()
            .filter(c -> c.getId() == id)
            .findFirst()
            .orElseThrow(CandidatoNaoEncontradoException::new);
    }

    public void deletar(int id) {
        Candidato candidato = buscarPorId(id);
        bancoSimulado.remove(candidato);
    }

    public List<Candidato> buscarPorPartido(String partido) {
        return bancoSimulado.stream()
            .filter(c -> c.getPartido().equalsIgnoreCase(partido))
            .toList();
    }

    public List<Candidato> buscarPorCategoria(String categoria) {
        return bancoSimulado.stream()
            .filter(c -> c.getCategoria().equalsIgnoreCase(categoria))
            .toList();
    }

    public List<Candidato> classificar(String categoria, String partido) {
        return bancoSimulado.stream()
            .filter(c -> c.getCategoria().equalsIgnoreCase(categoria)
                      && c.getPartido().equalsIgnoreCase(partido))
            .toList();
    }
}
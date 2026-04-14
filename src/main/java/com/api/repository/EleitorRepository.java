package com.api.repository;

import java.util.ArrayList;
import java.util.List;

import com.api.models.Eleitor;

public class EleitorRepository {
    private final List<Eleitor> bancoSimulado = new ArrayList<>();

    public void salvar(Eleitor eleitor) {
        bancoSimulado.add(eleitor);
    }

    public List<Eleitor> buscarTodos() {
        return bancoSimulado;
    }

    public Eleitor buscarPorId(int id) {
        return bancoSimulado.stream()
            .filter(e -> e.getId() == id)
            .findFirst()
            .orElse(null);
    }
}
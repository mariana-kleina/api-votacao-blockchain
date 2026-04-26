package com.api.service;

import java.util.List;

import com.api.exceptions.ValidationException;
import com.api.models.Candidato;
import com.api.repository.CandidatoRepository;

public class CandidatoService {

    private final CandidatoRepository repository = new CandidatoRepository();

    public void cadastrar(Candidato c) {

        if (c.getNome() == null || c.getNome().isBlank())
            throw new ValidationException("Nome é obrigatório");

        if (c.getPartido() == null || c.getPartido().isBlank())
            throw new ValidationException("Partido é obrigatório");

        if (c.getCategoria() == null || c.getCategoria().isBlank())
            throw new ValidationException("Categoria é obrigatória");

        if (c.getNumero() <= 0)
            throw new ValidationException("Número inválido");

        repository.salvar(c);
    }

    public List<Candidato> listar() {
        return repository.buscarTodos();
    }

    public Candidato buscar(int id) {
        return repository.buscarPorId(id);
    }

    public void deletar(int id) {
        repository.deletar(id);
    }
}
package com.api.controller;

import com.api.exceptions.IdadeInvalidaException;
import com.api.models.Eleitor;
import com.api.repository.EleitorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EleitorController {
    private final EleitorRepository repository = new EleitorRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    public String cadastrar(String json) throws Exception {
        Eleitor novoEleitor = mapper.readValue(json, Eleitor.class);

        if (novoEleitor.getIdade() < 16) {
            throw new IdadeInvalidaException("Eleitor inapto: idade mínima é 16 anos.");
        }

        repository.salvar(novoEleitor);
        return "Sucesso: Eleitor cadastrado.";
    }

    public String listar() throws Exception {
        return mapper.writeValueAsString(repository.buscarTodos());
    }

    public void deletar(int id) {
        // Lógica para deletar será implementada 
    }
}
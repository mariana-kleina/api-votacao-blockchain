package com.api.controller;

import com.api.models.Candidato;
import com.api.repository.CandidatoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class CandidatoController {

    private final CandidatoRepository repository = new CandidatoRepository();
    private final ObjectMapper mapper = new ObjectMapper();

   
    public String cadastrar(String json) throws Exception {
        Candidato candidato = mapper.readValue(json, Candidato.class);

        if (candidato.getNome() == null || candidato.getNome().isEmpty()) {
            throw new RuntimeException("Nome do candidato é obrigatório.");
        }

        if (candidato.getPartido() == null || candidato.getPartido().isEmpty()) {
            throw new RuntimeException("Partido é obrigatório.");
        }

        repository.salvar(candidato);
        return "Sucesso: Candidato cadastrado.";
    }

   
    public String listar() throws Exception {
        List<Candidato> lista = repository.buscarTodos();
        return mapper.writeValueAsString(lista);
    }

   
    public String buscarPorId(int id) throws Exception {
        Candidato candidato = repository.buscarPorId(id);
        return mapper.writeValueAsString(candidato);
    }

    public String buscarPorPartido(String partido) throws Exception {
        return mapper.writeValueAsString(repository.buscarPorPartido(partido));
    }


    public String buscarPorCategoria(String categoria) throws Exception {
        return mapper.writeValueAsString(repository.buscarPorCategoria(categoria));
    }


    public String classificar(String categoria, String partido) throws Exception {
        return mapper.writeValueAsString(repository.classificar(categoria, partido));
    }

   
    public String deletar(int id) {
        repository.deletar(id);
        return "Candidato removido com sucesso.";
    }
}
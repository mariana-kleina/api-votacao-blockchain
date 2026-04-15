package com.api.service;

import java.util.List;

import com.api.models.Candidato;
import com.api.repository.CandidatoRepository;

public class CandidatoService {

    private final CandidatoRepository repository = new CandidatoRepository();

  
    public void cadastrar(Candidato candidato) {
        repository.salvar(candidato);
    }

    
    public List<Candidato> listar() {
        return repository.buscarTodos();
    }

    
    public Candidato buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

   
    public void deletar(int id) {
        repository.deletar(id);
    }

    
    public List<Candidato> buscarPorPartido(String partido) {
        return repository.buscarPorPartido(partido);
    }

    public List<Candidato> buscarPorCategoria(String categoria) {
        return repository.buscarPorCategoria(categoria);
    }

    
    public List<Candidato> classificar(String categoria, String partido) {
        return repository.classificar(categoria, partido);
    }
}
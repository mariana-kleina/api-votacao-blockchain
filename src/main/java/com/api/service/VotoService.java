package com.api.service;

import java.util.List;

import com.api.models.Voto;
import com.api.repository.VotoRepository;

public class VotoService {
    private final VotoRepository repository = new VotoRepository();

    public void cadastrarVoto(Voto voto) {
        if (voto.getIdEleitor() <= 0) {
            throw new IllegalArgumentException("Eleitor inválido");
        }

        if (voto.getIdCandidato() <= 0) {
            throw new IllegalArgumentException("Candidato inválido");
        }
        
        repository.salvar(voto);
    }

    public List<Voto> listarVotos() {
        return repository.buscarTodos();
    }
}

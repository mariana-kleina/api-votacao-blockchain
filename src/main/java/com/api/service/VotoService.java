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

        if (voto.getNumeroCandidato() <= 0) {
            throw new IllegalArgumentException("Número do candidato inválido");
        }

        if (!repository.candidatoExiste(voto.getNumeroCandidato())) {
            throw new IllegalArgumentException("Candidato não existe");
        }

        repository.salvar(voto);
    }

    public List<Voto> listarVotos() {
        return repository.buscarTodos();
    }
}
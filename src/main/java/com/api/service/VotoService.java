package com.api.service;

import java.util.List;

import com.api.exceptions.ResourceNotFoundException;
import com.api.exceptions.ValidationException;
import com.api.models.Eleitor;
import com.api.models.Voto;
import com.api.repository.EleitorRepository;
import com.api.repository.VotoRepository;

public class VotoService {

    private final VotoRepository repository = new VotoRepository();
    private final EleitorRepository eleitorRepository = new EleitorRepository();
    private final BlockchainService blockchainService = new BlockchainService();

    public void cadastrarVoto(Voto voto) {

        if (voto.getIdEleitor() <= 0) {
            throw new ValidationException("ID do eleitor inválido");
        }

        if (voto.getNumeroCandidato() <= 0) {
            throw new ValidationException("Número do candidato inválido");
        }

        if (!repository.candidatoExiste(voto.getNumeroCandidato())) {
            throw new ResourceNotFoundException("Candidato não encontrado");
        }

        repository.salvar(voto);

        Eleitor eleitor = eleitorRepository.buscarPorId(voto.getIdEleitor());

        if (eleitor == null) {
            throw new ResourceNotFoundException("Eleitor não encontrado");
        }

        blockchainService.registrarVoto(eleitor.getCpf(), voto.getNumeroCandidato());
    }

    public List<Voto> listarVotos() {
        return repository.buscarTodos();
    }

    public Voto buscarPorId(int id) {
        Voto voto = repository.buscarPorId(id);

        if (voto == null) {
            throw new ResourceNotFoundException("Voto não encontrado");
        }

        return voto;
    }

    public void deletar(int id) {
        if (!repository.deletar(id)) {
            throw new ResourceNotFoundException("Voto não encontrado");
        }
    }
}
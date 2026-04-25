package com.api.service;

import java.util.List;

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
            throw new IllegalArgumentException("Eleitor inválido");
        }

        if (voto.getNumeroCandidato() <= 0) {
            throw new IllegalArgumentException("Número do candidato inválido");
        }

        if (!repository.candidatoExiste(voto.getNumeroCandidato())) {
            throw new IllegalArgumentException("Candidato não existe");
        }

        repository.salvar(voto);

        // REGISTRA NA BLOCKCHAIN APÓS SALVAR NO BANCO
        Eleitor eleitor = eleitorRepository.buscarPorId(voto.getIdEleitor());
        if (eleitor != null) {
            blockchainService.registrarVoto(eleitor.getCpf(), voto.getNumeroCandidato());
        }
    }

    public List<Voto> listarVotos() {
        return repository.buscarTodos();
    }

    public Voto buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    public boolean deletar(int id) {
        return repository.deletar(id);
    }
}
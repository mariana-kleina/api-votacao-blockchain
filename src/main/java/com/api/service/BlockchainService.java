package com.api.service;

import com.api.models.Eleitor;
import com.api.models.Voto;
import com.api.models.VotoBloco;
import com.api.repository.BlockchainRepository;
import com.api.repository.EleitorRepository;
import com.api.repository.VotoRepository;
import com.api.util.HashUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BlockchainService {

    private final BlockchainRepository repository = BlockchainRepository.getInstancia();
    private final EleitorRepository eleitorRepository = new EleitorRepository();
    private final VotoRepository votoRepository = new VotoRepository();

    public void registrarVoto(String cpf, int numeroCandidato) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String votoHash = HashUtil.gerarHash(numeroCandidato + cpf + timestamp);

        VotoBloco votoBloco = new VotoBloco(cpf, numeroCandidato, votoHash, timestamp);
        repository.adicionarVoto(votoBloco);
    }

    public VotoBloco verificarEleitor(String cpf) {
        if (!repository.eleitorVotou(cpf)) {
            return null;
        }
        return repository.buscarVotoEleitor(cpf);
    }

    public int votosPorCandidato(int numeroCandidato) {
        return repository.votosPorCandidato(numeroCandidato);
    }

    public int totalDeVotos() {
        return repository.totalDeVotos();
    }

    public com.api.models.Blockchain getBlockchain() {
        return repository.getBlockchain();
    }

    public void reconstruirDoBanco() {
        List<Voto> votos = votoRepository.buscarTodos();

        for (Voto voto : votos) {
            Eleitor eleitor = eleitorRepository.buscarPorId(voto.getIdEleitor());
            if (eleitor != null) {
                registrarVoto(eleitor.getCpf(), voto.getNumeroCandidato());
            }
        }

        System.out.println("Blockchain reconstruída com " + totalDeVotos() + " voto(s).");
    }
}
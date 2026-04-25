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

    // Chamado pelo VotoService após salvar o voto no banco
    public void registrarVoto(String cpf, int numeroCandidato) {
        String candidatoHash = HashUtil.gerarHash(String.valueOf(numeroCandidato));
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        VotoBloco votoBloco = new VotoBloco(cpf, candidatoHash, timestamp);
        repository.adicionarVoto(votoBloco);
    }

    // Verifica se eleitor votou — retorna os dados públicos ou null
    public VotoBloco verificarEleitor(String cpf) {
        if (!repository.eleitorVotou(cpf)) {
            return null; // Handler trata o null e retorna a mensagem
        }
        return repository.buscarVotoEleitor(cpf);
    }

    // Total de votos de um candidato (recalcula o hash para comparar)
    public int votosPorCandidato(int numeroCandidato) {
        String candidatoHash = HashUtil.gerarHash(String.valueOf(numeroCandidato));
        return repository.votosPorCandidato(candidatoHash);
    }

    // Total geral de votos na cadeia
    public int totalDeVotos() {
        return repository.totalDeVotos();
    }

    // Retorna todos os blocos fechados
    public com.api.models.Blockchain getBlockchain() {
        return repository.getBlockchain();
    }

    // Chamado no Main ao iniciar o servidor — reconstrói a cadeia do banco
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
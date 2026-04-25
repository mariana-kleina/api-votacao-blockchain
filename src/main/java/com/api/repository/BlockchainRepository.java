package com.api.repository;

import com.api.models.Bloco;
import com.api.models.Blockchain;
import com.api.models.VotoBloco;
import com.api.util.HashUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BlockchainRepository {

    private static final int LIMITE_VOTOS_POR_BLOCO = 5;

    // Singleton — uma única instância durante toda execução do servidor
    private static BlockchainRepository instancia;

    private List<Bloco> cadeia;
    private List<VotoBloco> votosPendentes; // votos do bloco ainda aberto

    private BlockchainRepository() {
        this.cadeia = new ArrayList<>();
        this.votosPendentes = new ArrayList<>();
    }

    public static BlockchainRepository getInstancia() {
        if (instancia == null) {
            instancia = new BlockchainRepository();
        }
        return instancia;
    }

    // Adiciona um voto ao bloco atual. Se atingir o limite, fecha o bloco.
    public void adicionarVoto(VotoBloco votoBloco) {
        votosPendentes.add(votoBloco);

        if (votosPendentes.size() >= LIMITE_VOTOS_POR_BLOCO) {
            fecharBloco();
        }
    }

    // Fecha o bloco atual e inicia um novo
    private void fecharBloco() {
    int indice = cadeia.size() + 1;
    String hashAnterior = cadeia.isEmpty() ? "0000" : cadeia.get(cadeia.size() - 1).getHashAtual();
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

    // Calcula o hash com base em todos os dados do bloco
    StringBuilder dados = new StringBuilder();
    dados.append(indice);
    dados.append(hashAnterior);
    dados.append(timestamp);
    for (VotoBloco v : votosPendentes) {
        dados.append(v.getCpfEleitor());
        dados.append(v.getCandidatoHash());
        dados.append(v.getTimestamp());
    }
    String hashAtual = HashUtil.gerarHash(dados.toString());

    Bloco bloco = new Bloco(
            indice,
            hashAnterior,
            hashAtual,        // agora calculado corretamente
            new ArrayList<>(votosPendentes),
            timestamp,
            votosPendentes.size()
    );

    cadeia.add(bloco);
    votosPendentes.clear();
}

    // Verifica se o eleitor já votou (busca na cadeia e nos pendentes)
    public boolean eleitorVotou(String cpf) {
        for (Bloco bloco : cadeia) {
            for (VotoBloco voto : bloco.getVotos()) {
                if (voto.getCpfEleitor().equals(cpf)) return true;
            }
        }
        for (VotoBloco voto : votosPendentes) {
            if (voto.getCpfEleitor().equals(cpf)) return true;
        }
        return false;
    }

    // Retorna o VotoBloco de um eleitor específico
    public VotoBloco buscarVotoEleitor(String cpf) {
        for (Bloco bloco : cadeia) {
            for (VotoBloco voto : bloco.getVotos()) {
                if (voto.getCpfEleitor().equals(cpf)) return voto;
            }
        }
        for (VotoBloco voto : votosPendentes) {
            if (voto.getCpfEleitor().equals(cpf)) return voto;
        }
        return null;
    }

    // Conta votos de um candidato pelo hash
    public int votosPorCandidato(String candidatoHash) {
        int total = 0;
        for (Bloco bloco : cadeia) {
            for (VotoBloco voto : bloco.getVotos()) {
                if (voto.getCandidatoHash().equals(candidatoHash)) total++;
            }
        }
        for (VotoBloco voto : votosPendentes) {
            if (voto.getCandidatoHash().equals(candidatoHash)) total++;
        }
        return total;
    }

    // Conta todos os votos registrados
    public int totalDeVotos() {
        int total = 0;
        for (Bloco bloco : cadeia) {
            total += bloco.getTotalVotos();
        }
        total += votosPendentes.size();
        return total;
    }

    // Retorna a blockchain completa como model
    public Blockchain getBlockchain() {
        return new Blockchain(cadeia);
    }

    public List<Bloco> getCadeia() { return cadeia; }
    public List<VotoBloco> getVotosPendentes() { return votosPendentes; }
}
package com.api.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.api.models.Blockchain;
import com.api.models.Bloco;
import com.api.models.VotoBloco;
import com.api.util.HashUtil;

public class BlockchainRepository {

    private static final int LIMITE_VOTOS_POR_BLOCO = 3;

    private static BlockchainRepository instancia;

    private List<Bloco> cadeia;
    private List<VotoBloco> votosPendentes;

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

    public void adicionarVoto(VotoBloco votoBloco) {
        votosPendentes.add(votoBloco);

        if (votosPendentes.size() >= LIMITE_VOTOS_POR_BLOCO) {
            fecharBloco();
        }
    }

    private void fecharBloco() {
        int indice = cadeia.size() + 1;
        String hashAnterior = cadeia.isEmpty() ? "0000" : cadeia.get(cadeia.size() - 1).getHashAtual();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        StringBuilder dados = new StringBuilder();
        dados.append(indice);
        dados.append(hashAnterior);
        dados.append(timestamp);
        for (VotoBloco v : votosPendentes) {
            dados.append(v.getCpfEleitor());
            dados.append(v.getNumeroCandidato());
            dados.append(v.getVotoHash());
            dados.append(v.getTimestamp());
        }
        String hashAtual = HashUtil.gerarHash(dados.toString());

        Bloco bloco = new Bloco(
                indice,
                hashAnterior,
                hashAtual,
                new ArrayList<>(votosPendentes),
                timestamp,
                votosPendentes.size()
        );

        cadeia.add(bloco);
        votosPendentes.clear();
    }

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

    public int votosPorCandidato(int numeroCandidato) {
        int total = 0;
        for (Bloco bloco : cadeia) {
            for (VotoBloco voto : bloco.getVotos()) {
                if (voto.getNumeroCandidato() == numeroCandidato) total++;
            }
        }
        for (VotoBloco voto : votosPendentes) {
            if (voto.getNumeroCandidato() == numeroCandidato) total++;
        }
        return total;
    }

    public int totalDeVotos() {
        int total = 0;
        for (Bloco bloco : cadeia) {
            total += bloco.getTotalVotos();
        }
        total += votosPendentes.size();
        return total;
    }

    public Blockchain getBlockchain() {
        return new Blockchain(cadeia);
    }

    public List<Bloco> getCadeia() { return cadeia; }
    public List<VotoBloco> getVotosPendentes() { return votosPendentes; }
}
package com.api.service;

import java.util.List;

import com.api.exceptions.IdadeInvalidaException;
import com.api.exceptions.ResourceNotFoundException;
import com.api.exceptions.ValidationException;
import com.api.models.Eleitor;
import com.api.repository.EleitorRepository;

public class EleitorService {

    private final EleitorRepository repository = new EleitorRepository();
    private final ViaCepService viaCepService = new ViaCepService();

    public void cadastrar(Eleitor eleitor) {
        validar(eleitor);
        repository.salvar(eleitor);
    }

    public List<Eleitor> listar() {
        return repository.buscarTodos();
    }

    public Eleitor buscarPorId(int id) {
        Eleitor eleitor = repository.buscarPorId(id);
        if (eleitor == null) {
            throw new ResourceNotFoundException("Eleitor não encontrado");
        }
        return eleitor;
    }

    public void atualizar(int id, Eleitor eleitor) {
        validar(eleitor);
        repository.atualizar(id, eleitor);
    }

    public void deletar(int id) {
        repository.deletar(id);
    }

    // Validações de negócio + integração com ViaCEP
    private void validar(Eleitor e) {

        if (e.getNome() == null || e.getNome().isBlank())
            throw new ValidationException("Nome é obrigatório");

        if (e.getCpf() == null || !e.getCpf().matches("\\d{11}"))
            throw new ValidationException("CPF inválido! Digite apenas os 11 números.");

        if (e.getIdade() < 16)
            throw new IdadeInvalidaException("Eleitor inapto: idade mínima é 16 anos.");

        if (e.getCep() == null || e.getCep().isBlank())
            throw new ValidationException("CEP é obrigatório para validação da zona eleitoral.");

        // Consulta o ViaCEP para obter a cidade pelo CEP informado
        String cidade = viaCepService.buscarCidadePorCep(e.getCep());

        // Preenche a cidade automaticamente no objeto
        e.setCidade(cidade);

        // Regra de negócio: apenas eleitores de Curitiba podem se cadastrar
        if (!"Curitiba".equalsIgnoreCase(cidade)) {
            throw new ValidationException(
                "Eleitor inapto: Este sistema de votação é exclusivo para o município de Curitiba (Identificado: " + cidade + ")."
            );
        }
    }
}

package com.api.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.api.exceptions.ValidationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ViaCepService {

    // Usamos o Jackson (ObjectMapper) que o Otávio configurou para mapear o JSON do ViaCEP
    private final ObjectMapper mapper = new ObjectMapper();

    public String buscarCidadePorCep(String cep) {
        // Limpa o CEP tirando traços ou espaços que o usuário possa ter digitado
        String cepLimpo = cep.replaceAll("\\D", "");

        if (cepLimpo.length() != 8) {
            throw new ValidationException("Formato de CEP inválido! Deve conter 8 dígitos.");
        }

        try {
            // Monta a URL oficial do ViaCEP
            URL url = new URL("https://viacep.com.br/ws/" + cepLimpo + "/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // Se a API demorar mais de 5s, cancela
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() != 200) {
                throw new ValidationException("Falha ao conectar com o serviço ViaCEP.");
            }

            // Lê a resposta do ViaCEP
            try (InputStream is = conn.getInputStream()) {
                JsonNode node = mapper.readTree(is);

                // O ViaCEP devolve um campo "erro: true" se o CEP não existir no mapa
                if (node.has("erro") && node.get("erro").asBoolean()) {
                    throw new ValidationException("CEP inexistente na base do ViaCEP.");
                }

                // Puxa o campo "localidade" do JSON recebido, que representa a Cidade
                return node.get("localidade").asText();
            }

        } catch (ValidationException e) {
            // Repassa o nosso erro de validação para o ExceptionHandlerUtil tratar
            throw e; 
        } catch (Exception e) {
            throw new RuntimeException("Erro na integração com o ViaCEP: " + e.getMessage());
        }
    }
}
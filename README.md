# API de Votação com Blockchain 🗳️

## Descrição do Projeto
Este sistema é uma API REST desenvolvida em Java para simular um processo de votação digital seguro. O projeto utiliza conceitos de blockchain para garantir a integridade e a imutabilidade dos votos registrados, assegurando que cada transação seja única e verificável.

## Tecnologias Utilizadas
* **Linguagem**: Java
* **Persistência de Dados**: Banco de dados via JDBC
* **Manipulação de JSON**: Biblioteca Jackson (Serialização e Desserialização)
* **Arquitetura**: Separação em camadas (Controllers, Models e Repository)

## Endpoints da API
Abaixo estão listadas as rotas principais da aplicação para a gestão do sistema:

### 👤 Módulo de Eleitores
* `GET /eleitores`: Retorna a lista completa de eleitores cadastrados.
* `GET /eleitores/{id}`: Busca os detalhes de um eleitor específico pelo ID.
* `POST /eleitores`: Cadastra um novo eleitor.
    * **Regra de Negócio**: Validação de idade mínima de 16 anos para estar apto a votar.
* `PUT /eleitores/{id}`: Atualiza as informações de um eleitor existente.
* `DELETE /eleitores/{id}`: Remove um eleitor da base de dados.

### 🗳️ Módulo de Votação e Blockchain
* `POST /votos`: Registra um novo voto, vinculando o eleitor ao seu candidato.
* `GET /blockchain`: Exibe a corrente de blocos e valida a integridade dos hashes gerados.

## Diferenciais Técnicos
* **Validação Real**: A API implementa regras de negócio ativas (como a idade mínima), evitando ser apenas um "CRUD vazio".
* **Integridade**: Uso de hashing para encadeamento de blocos, garantindo que os dados não sejam alterados.
* **Tratamento de Erros**: Implementação de exceções personalizadas para falhas em regras de negócio.

## Equipe
1. Igor
2. Laura Reded
3. Maiara Wojciekovski
4. Mariana Kleina
5. Otávio Wenzel
6. Rafael Villa

---
**Data Limite para Entrega**: 28/04/2026 às 18h.
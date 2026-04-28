# API de Votação com Blockchain 🗳️

## Descrição do Projeto
Este sistema é uma API REST desenvolvida em Java para simular um processo de votação digital seguro. O projeto utiliza conceitos de blockchain para garantir a integridade e a imutabilidade dos votos registrados, assegurando que cada transação seja única e verificável.

## Tecnologias Utilizadas
* **Linguagem**: Java
* **Persistência de Dados**: Banco de dados via JDBC
* **Manipulação de JSON**: Biblioteca Jackson (Serialização e Desserialização)
* **Arquitetura**: Separação em camadas (Controllers, Models e Repository), utilização de Singleton na blockchain para utilizar uma blockchain.

## Endpoints da API
Abaixo estão listadas as rotas principais da aplicação para a gestão do sistema:

### 👤 Módulo de Eleitores
* `POST /eleitor`: Cadastra um novo eleitor.
* `GET /eleitor`: Retorna a lista completa de eleitores cadastrados.
* `GET /eleitor/{id}`: Busca os detalhes de um eleitor específico pelo ID.
* `PUT /eleitor/{id}`: Atualiza as informações de um eleitor existente.
* `DELETE /eleitor/{id}`: Remove um eleitor da base de dados.

### 👤 Módulo de Candidatos
* `POST /candidato`: Cadastra um novo candidato.
* `GET /candidato`: Retorna a lista completa de candidatos cadastrados.
* `GET /candidato/{id}`: Busca os detalhes de um candidato específico pelo ID.
* `DELETE /candidato/{id}`: Remove um candidato da base de dados.

### 🗳️ Módulo de Votos
* `POST /voto`: Cadastra um novo voto.
* `GET /voto`: Retorna a lista completa dos votos cadastrados.
* `GET /voto/{id}`: Busca os detalhes de um voto específico pelo ID.
* `DELETE /voto/{id}`: Remove um voto da base de dados.


### 🗳️ Módulo de Blockchain
* `GET /blockchain`: Exibe a corrente de blocos completa gerada.
* `GET /blockchain/total`: Exibe o total de votos realizados dentro da blockchain.
* `GET /blockchain/eleitor/{id}`: Exibe os dados do voto do eleitor selecionado.
* `GET /blockchain/candidato/numeroCandidato`: Exibe a quantidade de votos do candidato selecionado.

## Diferenciais Técnicos
* **Validação Real**: A API implementa regras de negócio ativas (como a idade mínima, não podendo duplicar o voto do mesmo eleitor, não permite a criação de eleitores com mesmo identificador nem candidatos com mesmo numero de voto), evitando ser apenas um "CRUD vazio".
* **Integridade**: Uso de hashing para encadeamento de blocos, garantindo que os dados não sejam alterados.
* **Tratamento de Erros**: Implementação de exceções personalizadas para falhas em regras de negócio.

## Equipe
1. Igor Hey Matos
2. Laura Reded
3. Maiara Wojciekovski
4. Mariana Kleina
5. Otávio Wenzel
6. Rafael Villa

---
**Data Limite para Entrega**: 28/04/2026 às 18h..

# API de Votação com Blockchain 🗳️

## Descrição do Projeto
Este sistema é uma API REST desenvolvida em Java para simular um processo de votação digital seguro. O projeto utiliza conceitos de blockchain para garantir a integridade e a imutabilidade dos votos registrados, assegurando que cada transação seja única e verificável. Além disso, integra mensageria assíncrona com RabbitMQ e consumo de API externa para validação de zona eleitoral.

## Tecnologias Utilizadas
* **Linguagem**: Java 17
* **Servidor HTTP**: `com.sun.net.httpserver.HttpServer` (sem frameworks externos)
* **Persistência de Dados**: H2 Database (banco embarcado) via JDBC puro
* **Manipulação de JSON**: Biblioteca Jackson (Serialização e Desserialização)
* **API Externa**: ViaCEP (`viacep.com.br`) — validação de CEP e cidade do eleitor
* **Mensageria**: RabbitMQ com `amqp-client 5.20.0` — Producer e Consumer na fila `fila-votos`
* **Blockchain**: Implementação própria em memória com hashing SHA-256 e padrão Singleton
* **Arquitetura**: Separação em camadas (Handler → Service → Repository)

## Pré-requisitos para executar
1. **Java 17+** instalado
2. **Maven** para gerenciar dependências
3. **RabbitMQ** rodando localmente na porta `5672`
   - Requer **Erlang** instalado previamente
   - Download: https://www.rabbitmq.com/download.html

## Endpoints da API
Abaixo estão listadas as rotas da aplicação. Todas as respostas seguem o padrão JSON:
```json
{
  "sucesso": true,
  "mensagem": "descrição",
  "dados": { ... }
}
```

### 👤 Módulo de Eleitores
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/eleitor` | Cadastra um novo eleitor. Valida CEP via ViaCEP e exige que a cidade seja Curitiba. |
| `GET` | `/eleitor` | Retorna a lista completa de eleitores cadastrados. |
| `GET` | `/eleitor/{id}` | Busca os detalhes de um eleitor específico pelo ID. |
| `PUT` | `/eleitor/{id}` | Atualiza as informações de um eleitor existente. |
| `DELETE` | `/eleitor/{id}` | Remove um eleitor da base de dados. |

**Exemplo de body para POST/PUT:**
```json
{
  "nome": "João Silva",
  "cpf": "12345678901",
  "idade": 25,
  "cep": "80010000"
}
```
> O campo `cidade` é preenchido automaticamente pela API via ViaCEP.

---

### 🧑‍💼 Módulo de Candidatos
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/candidato` | Cadastra um novo candidato. |
| `GET` | `/candidato` | Retorna a lista completa de candidatos cadastrados. |
| `GET` | `/candidato/{id}` | Busca os detalhes de um candidato específico pelo ID. |
| `DELETE` | `/candidato/{id}` | Remove um candidato da base de dados. |

**Exemplo de body para POST:**
```json
{
  "nome": "Maria Oliveira",
  "partido": "PSD",
  "categoria": "Prefeito",
  "numero": 15
}
```

---

### 🗳️ Módulo de Votos
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/voto` | Registra um voto. Cada eleitor só pode votar uma vez. Dispara mensagem no RabbitMQ. |
| `GET` | `/voto` | Retorna a lista completa de votos registrados. |

**Exemplo de body para POST:**
```json
{
  "idEleitor": 1,
  "numeroCandidato": 15
}
```

---

### ⛓️ Módulo de Blockchain
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/blockchain` | Exibe a corrente de blocos completa. Blocos são formados a cada 3 votos. |
| `GET` | `/blockchain/total` | Exibe o total de votos registrados na blockchain. |
| `GET` | `/blockchain/eleitor/{cpf}` | Verifica se um eleitor (pelo CPF) já votou e exibe o hash do voto. |
| `GET` | `/blockchain/candidato/{numero}` | Exibe a quantidade de votos de um candidato pelo número. |

> O número do candidato dentro de cada bloco é armazenado como **hash SHA-256**, garantindo o sigilo do voto.

---

## Fluxo de Mensageria (RabbitMQ)
Ao registrar um voto via `POST /voto`, o sistema executa automaticamente:
1. Salva o voto no banco H2
2. Registra o voto na blockchain em memória
3. **Envia uma mensagem** para a fila `fila-votos` (Producer)
4. O **Consumer**, rodando em Thread separada, recebe e loga a mensagem no console

```
[RabbitMQ Producer] Mensagem enviada: Voto registrado | Eleitor CPF: 12345678901 | Candidato: 15
[RabbitMQ Consumer] Novo voto recebido: Voto registrado | Eleitor CPF: 12345678901 | Candidato: 15
```

## Diferenciais Técnicos
* **Validação com API externa**: O CEP informado no cadastro do eleitor é validado em tempo real pelo ViaCEP. Apenas eleitores de Curitiba podem ser cadastrados.
* **Blockchain com SHA-256**: Os votos são agrupados em blocos encadeados por hash. O número do candidato é protegido e nunca exposto diretamente.
* **Mensageria assíncrona**: Cada voto registrado dispara uma mensagem no RabbitMQ, consumida em Thread paralela sem bloquear o servidor.
* **Validação Real**: Regras de negócio ativas — idade mínima de 16 anos, CPF único por eleitor, número único por candidato, um voto por eleitor.
* **Tratamento de Erros**: Exceções personalizadas com respostas HTTP adequadas (400, 404, 409, 500).
* **Persistência com reconstrução**: Ao reiniciar o servidor, a blockchain é reconstruída automaticamente a partir dos votos salvos no H2.

## Equipe
1. Igor Hey Matos
2. Laura Reded
3. Maiara Wojciekovski
4. Mariana Kleina
5. Otávio Wenzel
6. Rafael Villa

---
**Universidade Positivo — Engenharia de Software — Integração de Sistemas de Software**

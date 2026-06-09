package com.api.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class VotoProducer {

    // Nome da fila — deve ser idêntico ao usado no Consumer
    private static final String FILA = "fila-votos";

    public void enviarMensagem(String mensagem) {

        // Configura a conexão com o RabbitMQ rodando localmente
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        // try-with-resources: fecha a conexão e o canal automaticamente ao final
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declara a fila (se já existir, não faz nada; se não existir, cria)
            // durable=false → fila não sobrevive ao restart do RabbitMQ
            // exclusive=false → pode ser usada por outras conexões
            // autoDelete=false → não se apaga quando consumidor desconecta
            channel.queueDeclare(FILA, false, false, false, null);

            // Publica a mensagem na fila (exchange="" = direct ao nome da fila)
            channel.basicPublish("", FILA, null, mensagem.getBytes("UTF-8"));

            System.out.println("[RabbitMQ Producer] Mensagem enviada: " + mensagem);

        } catch (Exception e) {
            // Não derruba a API se o RabbitMQ não estiver rodando
            System.err.println("[RabbitMQ Producer] Falha ao enviar mensagem: " + e.getMessage());
        }
    }
}

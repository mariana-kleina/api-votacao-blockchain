package com.api.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class VotoConsumer {

    // Nome da fila — deve ser idêntico ao usado no Producer
    private static final String FILA = "fila-votos";

    public void iniciar() {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            // Conexão e canal ficam ABERTOS enquanto o servidor roda
            // (diferente do Producer que abre/fecha por mensagem)
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Garante que a fila existe antes de tentar consumir
            channel.queueDeclare(FILA, false, false, false, null);

            System.out.println("[RabbitMQ Consumer] Aguardando mensagens na fila: " + FILA);

            // Define o que fazer ao receber uma mensagem
            DeliverCallback aoReceber = (consumerTag, delivery) -> {
                String mensagem = new String(delivery.getBody(), "UTF-8");
                System.out.println("[RabbitMQ Consumer] Novo voto recebido: " + mensagem);
            };

            // Inicia o consumo; autoAck=true confirma recebimento automaticamente
            channel.basicConsume(FILA, true, aoReceber, consumerTag -> {
                System.out.println("[RabbitMQ Consumer] Consumidor cancelado: " + consumerTag);
            });

        } catch (Exception e) {
            System.err.println("[RabbitMQ Consumer] Falha ao iniciar consumidor: " + e.getMessage());
        }
    }
}

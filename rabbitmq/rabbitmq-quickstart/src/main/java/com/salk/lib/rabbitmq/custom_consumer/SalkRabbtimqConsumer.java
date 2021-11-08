package com.salk.lib.rabbitmq.custom_consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author salk
 */
public class SalkRabbtimqConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.168.1.36");
        connectionFactory.setPort(5670);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setConnectionTimeout(100000);

        // 创建一个连接
        Connection connection = connectionFactory.newConnection();

        // 创建一个channel
        Channel channel = connection.createChannel();

        // 声明交换机
        String exchangeName = "salk.customconsumer.direct";
        String exchangeType = "direct";
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, null);

        // 声明队列
        String queueName = "salk.customconsumer.queue";
        channel.queueDeclare(queueName, true, false, false, null);

        // 交换机绑定队列
        String routingKey = "salk.customconsumer.key";
        channel.queueBind(queueName, exchangeName, routingKey);

        channel.basicConsume(queueName, new SalkConsumer(channel));
    }

    static public class SalkConsumer extends DefaultConsumer {

        public SalkConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
            System.out.println("consumerTag:" + consumerTag);
            System.out.println("envelope:" + envelope);
            System.out.println("properties:" + properties);
            System.out.println("body:" + new String(body));

        }
    }
}

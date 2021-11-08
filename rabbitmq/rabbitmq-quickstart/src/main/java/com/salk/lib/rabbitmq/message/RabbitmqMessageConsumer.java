package com.salk.lib.rabbitmq.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author salk
 */
public class RabbitmqMessageConsumer {


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.168.1.36");
        connectionFactory.setPort(5670);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建一个channel
        Channel channel = connection.createChannel();

        String exchangeName = "salk.direct_exchange";
        String exchangeType = "direct";
        String queueName = "salk.directqueue";
        String routingKey = "salk.directchange.key";
        channel.exchangeDeclare(exchangeName, exchangeType, true, true, null);

        /**
         * 声明一个队列
         */
        channel.queueDeclare(queueName, true, false, true, null);

        /**
         * 队里和交换机绑定
         */
        channel.queueBind(queueName, exchangeName, routingKey);

        /**
         * 创建一个消费者
         */
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        /**
         * 开始消费
         */
        channel.basicConsume(queueName, true, queueingConsumer);


        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String reserveMsg = new String(delivery.getBody());
            Thread.sleep(50000L);
            System.out.println("encoding:" + delivery.getProperties().getContentEncoding());
            System.out.println("company:" + delivery.getProperties().getHeaders().get("company"));
            System.out.println("correlationId:" + delivery.getProperties().getCorrelationId());
        }
    }
}

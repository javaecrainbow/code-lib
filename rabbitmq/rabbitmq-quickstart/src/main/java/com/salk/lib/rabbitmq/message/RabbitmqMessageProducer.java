package com.salk.lib.rabbitmq.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author salk
 */
public class RabbitmqMessageProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
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

        Map<String, Object> headsMap = new HashMap<>();
        headsMap.put("company", "biyebao");
        headsMap.put("name", "salk");

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
            // 2标识持久化消息 1标识 服务重启后 消息不会被持久化
            .deliveryMode(2)
            // 消息过期 10s
            .expiration("10000").contentEncoding("utf-8").correlationId(UUID.randomUUID().toString()).headers(headsMap)
            .build();

        // 5:通过channel发送消息
        for (int i = 0; i < 5; i++) {
            String message = "hello--" + i;
            channel.basicPublish("salk.direct_exchange", "salk.directchange.key", basicProperties, message.getBytes());
        }

        // 6:关闭连接
        channel.close();
        connection.close();
    }
}

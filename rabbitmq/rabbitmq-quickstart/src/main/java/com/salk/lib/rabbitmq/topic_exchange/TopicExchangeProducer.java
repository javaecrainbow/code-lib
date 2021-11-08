package com.salk.lib.rabbitmq.topic_exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by smlz on 2019/9/28.
 */
public class TopicExchangeProducer {

    public static void main(String[] args) throws IOException, TimeoutException {

        //创建连接工厂
        ConnectionFactory connectionFactory  = new ConnectionFactory();
        connectionFactory.setHost("172.168.1.36");
        connectionFactory.setPort(5670);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建一个channel
        Channel channel = connection.createChannel();
        //发送消息
        String exchangeName = "salk.topic_exchange";
        String routingKey1 = "salk.key1";
        String routingKey2 = "salk.key2";
        String routingKey3 = "salk.key.key3";
        channel.basicPublish(exchangeName,routingKey1,null,"我是第一条消息".getBytes());
        channel.basicPublish(exchangeName,routingKey2,null,"我是第二条消息".getBytes());
        channel.basicPublish(exchangeName,routingKey3,null,"我是第三条消息".getBytes());
        channel.close();
        connection.close();
    }
}

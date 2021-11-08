package com.salk.lib.rabbitmq.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author salk
 */
public class DlxRabbitmqProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //设置连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.168.1.36");
        connectionFactory.setPort(5670);
        connectionFactory.setUsername("admin");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPassword("admin");
        connectionFactory.setConnectionTimeout(100000);

        //获取连接
        Connection connection = connectionFactory.newConnection();

        //获取一个channel
        Channel channel = connection.createChannel();

        //消息十秒没有被消费，那么就会转到死信队列上
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .expiration("10000")
                .build();

        //声明正常的队列
        String nomalExchangeName = "salk.nomaldlx.exchange";
        String routingKey = "salk.dlx.key1";

        String message = "我是测试的死信消息";
        for(int i=0;i<10;i++) {
            channel.basicPublish(nomalExchangeName,routingKey,basicProperties,message.getBytes());
        }
    }
}

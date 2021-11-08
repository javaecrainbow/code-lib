package com.salk.lib.rabbitmq.fanout_exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author salk
 */
public class FanoutExchangeProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.168.1.36");
        //vip address
        //connectionFactory.setHost("172.168.1.20");
        //HA端口
        connectionFactory.setPort(5670);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setConnectionTimeout(100000);
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建一个channel
        Channel channel = connection.createChannel();

        //定义交换机名称
        String exchangeName = "salk.fanoutexchange";

        channel.basicPublish(exchangeName, "123", null, "我是第一条消息".getBytes());
        channel.basicPublish(exchangeName, "456", null, "我是第二条消息".getBytes());
        channel.basicPublish(exchangeName, "789", null, "我是第三条消息".getBytes());
        channel.close();
        connection.close();
    }
}

package com.salk.lib.rabbitmq.return_listener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author salk
 */
public class RetruningListenerConsumer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.168.1.36");
        connectionFactory.setPort(5670);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setConnectionTimeout(100000);

        //创建一个连接
        Connection connection = connectionFactory.newConnection();

        //创建一个channel
        Channel channel = connection.createChannel();

        //声明交换机
        String exchangeName = "salk.retrun.direct";
        channel.exchangeDeclare(exchangeName,"direct",true,false,null);

        //声明队列
        String queueName = "t04.retrunlistener.queue";
        channel.queueDeclare(queueName,true,false,false,null);

        //声明一个绑定
        String routingKey = "salk.retrun.key.ok";
        channel.queueBind(queueName,exchangeName,routingKey);

        //创建一个消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        channel.basicConsume(queueName,true,queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            System.out.println("接受的消息:"+new String(delivery.getBody()));
        }

    }
}

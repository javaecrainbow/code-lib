package com.salk.lib.rabbitmq.fanout_exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * @author salk
 */
public class FanoutExchangeConsumer {


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
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

        //声明交换机
        String exchangeName = "salk.fanoutexchange";
        String exchangeType = "fanout";
        channel.exchangeDeclare(exchangeName, exchangeType, true, true, null);

        //声明队列
        String queueName = "salk.fanout.queue";
        channel.queueDeclare(queueName, true, false, true, null);

        //声明绑定关系
        String bingdingStr = "jj";
        channel.queueBind(queueName, exchangeName, bingdingStr);

        //声明一个消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //开始消费
        /**
         * 开始消费
         */
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            System.out.println("接受到消息:" + new String(delivery.getBody()));
        }
    }
}

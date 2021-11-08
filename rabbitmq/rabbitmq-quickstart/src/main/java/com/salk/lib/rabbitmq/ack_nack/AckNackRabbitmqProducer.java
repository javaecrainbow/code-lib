package com.salk.lib.rabbitmq.ack_nack;

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
 * 手动签收(ack) 和nack(requueu===>true|fasle)
 * @author salk
 */
public class AckNackRabbitmqProducer {

    public static void main(String[] args) throws IOException, TimeoutException {

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

        //定义交换机的名称
        String exchangeName = "";

        String routingKey = "salk-queue-1";

        String msgBody = "你好 nack===========";


        for(int i=0;i<10;i++) {

            Map<String,Object> infoMap = new HashMap<>();
            infoMap.put("mark",i);
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                    //消息持久化
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .correlationId(UUID.randomUUID().toString())
                    .headers(infoMap)
                    .build();
            channel.basicPublish(exchangeName,routingKey,basicProperties,(msgBody+i).getBytes());
        }
    }
}

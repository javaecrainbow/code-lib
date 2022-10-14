package com.salk.lib.rabbitmq.ttl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by smlz on 2019/9/30.
 */
public class TTLRabbitmqProducer {

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
        String exchangeName = "salk.ttl.direct";

        String routingKey = "salk.ttl.key";

        String queueName = "salk.ttl.queue";

        //申明交换机
        channel.exchangeDeclare(exchangeName,"direct",true,false,null);

        //申明队列
        Map<String,Object> queueArgs = new HashMap<>();
        //设置队列中的消息10s没有被消费就会过期
        queueArgs.put("x-message-ttl",10000);
        //队列的长度
        queueArgs.put("x-max-length",4);
        channel.queueDeclare(queueName,true,false,false,queueArgs);
        channel.txSelect();
        channel.txCommit();
        channel.txRollback();
        //绑定
        channel.queueBind(queueName,exchangeName,routingKey);

        String msgBody = "你好salk";
        for(int i=0;i<10;i++) {
            channel.basicPublish(exchangeName,routingKey,null,(msgBody+i).getBytes());
        }
    }
}

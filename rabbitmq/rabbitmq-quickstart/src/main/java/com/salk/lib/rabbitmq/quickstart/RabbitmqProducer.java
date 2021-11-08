package com.salk.lib.rabbitmq.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author salk
 */
public class RabbitmqProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory  = new ConnectionFactory();
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
        //声明队列
        channel.queueDeclare("salk-queue-1", true, false, false,null);
        //5:通过channel发送消息
        for(int i=0;i<5;i++) {
            String message = "hello--"+i;
            channel.basicPublish("","salk-queue-1",null,message.getBytes());
        }
        //6:关闭连接
        channel.close();
        connection.close();
        System.out.println("send success===================");
    }
}

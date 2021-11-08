package com.salk.lib.rabbitmq.consumer_limit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * qos 限流
 * @author salk
 */
public class SalkQosRabbtimqConsumer {

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

        //声明交换机
        String exchangeName = "salk.qos.direct";
        String exchangeType = "direct";
        channel.exchangeDeclare(exchangeName,exchangeType,true,false,null);

        //声明队列
        String queueName = "salk.qos.queue";
        channel.queueDeclare(queueName,true,false,false,null);

        //交换机绑定队列
        String routingKey = "salk.qos.key";
        channel.queueBind(queueName,exchangeName,routingKey);


        /**
         * 限流设置:  prefetchSize：每条消息大小的设置
         * prefetchCount:标识每次推送多少条消息 一般是一条
         * global:false标识channel级别的  true:标识消费的级别的
         */
        channel.basicQos(0,1,false);

        /**
         * 消费端限流 需要关闭消息自动签收
         */
        channel.basicConsume(queueName,false,new SalkQosConsumer(channel));
    }
    static public class SalkQosConsumer extends DefaultConsumer {

        private Channel channel;

        public SalkQosConsumer(Channel channel) {
            super(channel);
            this.channel = channel;
        }

        @Override
        public void handleDelivery(String consumerTag,
                                   Envelope envelope,
                                   AMQP.BasicProperties properties,
                                   byte[] body)
                throws IOException
        {
            System.out.println("consumerTag:"+consumerTag);
            System.out.println("envelope:"+envelope);
            System.out.println("properties:"+properties);
            System.out.println("body:"+new String(body));

            /**
             * multiple:false标识不批量签收
             */
            channel.basicAck(envelope.getDeliveryTag(),false);
        }
    }
}

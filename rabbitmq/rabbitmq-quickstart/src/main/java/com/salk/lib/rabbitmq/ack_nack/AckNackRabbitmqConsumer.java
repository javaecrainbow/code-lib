package com.salk.lib.rabbitmq.ack_nack;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author salk
 */
public class AckNackRabbitmqConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.168.1.36");
        connectionFactory.setPort(5670);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setConnectionTimeout(100000);

        // 创建一个连接
        Connection connection = connectionFactory.newConnection();

        // 创建一个channel
        Channel channel = connection.createChannel();

        // 声明队列
        String queueName = "salk-queue-1";
        channel.queueDeclare(queueName, true, false, false, null);

        /**
         * 消费端限流 需要关闭消息自动签收
         */
        channel.basicConsume(queueName, false, new AckConsumer(channel));
    }

    static class AckConsumer extends DefaultConsumer {

        private Channel channel;

        public AckConsumer(Channel channel) {
            super(channel);
            this.channel = channel;
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
            try {
                // 模拟业务
                Integer mark = (Integer)properties.getHeaders().get("mark");
                if (mark != 0) {
                    System.out.println("消费消息:" + new String(body));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } else {
                    throw new RuntimeException("模拟业务异常");
                }
            } catch (Exception e) {
                System.out.println("异常消费消息:" + new String(body));
                // 重回队列
                // channel.basicNack(envelope.getDeliveryTag(),false,true);
                // 不重回队列会进入死信队列
                channel.basicNack(envelope.getDeliveryTag(), false, false);
                //不进入死信队列
                //channel.basicReject(envelope.getDeliveryTag(),false);

            }
        }
    }
}

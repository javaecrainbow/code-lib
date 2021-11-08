package com.salk.lib.rabbitmq.dlx;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author salk
 */
public class DlxRabbitmqConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {

        // 设置连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.168.1.36");
        connectionFactory.setPort(5670);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setConnectionTimeout(100000);
        // 获取连接
        Connection connection = connectionFactory.newConnection();

        // 获取一个channel
        Channel channel = connection.createChannel();

        // 声明正常的队列
        String nomalExchangeName = "salk.nomaldlx.exchange";
        String exchangeType = "topic";
        String nomalqueueName = "salk.nomaldex.queue";
        String routingKey = "salk.dlx.#";

        // 申明死信队列
        String dlxExhcangeName = "salk.dlx.exchange";
        String dlxQueueName = "salk.dlx.queue";

        channel.exchangeDeclare(nomalExchangeName, exchangeType, true, false, null);

        Map<String, Object> queueArgs = new HashMap<>();
        // 正常队列上绑定死信队列
        queueArgs.put("x-dead-letter-exchange", dlxExhcangeName);
        queueArgs.put("x-max-length", 4);
        channel.queueDeclare(nomalqueueName, true, false, false, queueArgs);
        channel.queueBind(nomalqueueName, nomalExchangeName, routingKey);

        // 声明死信队列
        channel.exchangeDeclare(dlxExhcangeName, exchangeType, true, false, null);
        channel.queueDeclare(dlxQueueName, true, false, false, null);
        channel.queueBind(dlxQueueName, dlxExhcangeName, "#");

        channel.basicConsume(nomalqueueName, false, new DlxConsumer(channel));

    }

    static public class DlxConsumer extends DefaultConsumer {

        private Channel channel;

        public DlxConsumer(Channel channel) {
            super(channel);
            this.channel = channel;
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
            System.out.println("接受到消息:" + new String(body));
            // 消费端拒绝签收，并且不支持重回队列，那么该条消息就是一条死信消息
            //channel.basicNack(envelope.getDeliveryTag(), false, false);

             channel.basicAck(envelope.getDeliveryTag(),false);
        }
    }
}

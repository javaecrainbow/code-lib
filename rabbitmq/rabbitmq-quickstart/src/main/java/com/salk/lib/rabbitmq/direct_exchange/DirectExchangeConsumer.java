package com.salk.lib.rabbitmq.direct_exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * @author salk
 */
public class DirectExchangeConsumer {

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

        String exchangeName = "salk.direct_exchange";
        String exchangeType = "direct";
        String queueName = "salk.directqueue";
        String routingKey = "salk.directchange.key";
        /**
         * 声明一个交换机
         * exchange:交换机的名称
         * type:交换机的类型 常见的有direct,fanout,topic等
         * durable:设置是否持久化。durable设置为true时表示持久化，反之非持久化.持久化可以将交换器存入磁盘，在服务器重启的时候不会丢失相关信息
         * autodelete:设置是否自动删除。autoDelete设置为true时，则表示自动删除。自动删除的前提是至少有一个队列或者交换器与这个交换器绑定，之后，所有与这个交换器绑定的队列或者交换器都与此解绑。
         * 不能错误的理解—当与此交换器连接的客户端都断开连接时，RabbitMq会自动删除本交换器
         * arguments:其它一些结构化的参数，比如：alternate-exchange
         */
        channel.exchangeDeclare(exchangeName, exchangeType, true, true, null);

        /**
         * 声明一个队列
         * durable:表示rabbitmq关闭删除队列
         * autodelete:表示没有程序和队列建立连接 那么就会自动删除队列
         *
         */
        channel.queueDeclare(queueName, true, false, true, null);

        /**
         * 队里和交换机绑定
         */
        channel.queueBind(queueName, exchangeName, routingKey);

        /**
         * 创建一个消费者
         */
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        /**
         * 开始消费
         */
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String receiverMsg = new String(delivery.getBody());
            System.out.println("消费消息:-----" + receiverMsg);
        }

    }
}

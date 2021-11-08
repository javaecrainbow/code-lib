package com.salk.lib.rabbitmq.return_listener;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * mandotory and return listener
 * @author salk
 */
public class ReturingListenerProducer {

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

        //准备发送消息
        String exchangeName = "salk.retrun.direct";
        String okRoutingKey = "salk.retrun.key.ok";
        String errorRoutingKey = "salk.retrun.key.error";

        /**
         * 设置监听不可达消息
         */
        channel.addReturnListener(new SalkReturnListener());


        //设置消息属性
        Map<String,Object> tulingInfo = new HashMap<>();
        tulingInfo.put("company","salk");
        tulingInfo.put("location","长沙");

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .correlationId(UUID.randomUUID().toString())
                .timestamp(new Date())
                .headers(tulingInfo)
                .build();

        String msgContext = "你好 ...."+System.currentTimeMillis();

        /**
         * 发送消息
         * mandatory:该属性设置为false,那么不可达消息就会被mq broker给删除掉
         *          :true,那么mq会调用我们得retrunListener 来告诉我们业务系统 说该消息
         *          不能成功发送.
         */
        channel.basicPublish(exchangeName,okRoutingKey,true,basicProperties,msgContext.getBytes());


        String errorMsg1 = "你好  mandotory为false...."+System.currentTimeMillis();

        //错误发送   mandotory为false
        channel.basicPublish(exchangeName,errorRoutingKey,false,basicProperties,errorMsg1.getBytes());

        String errorMsg2 = "你好 mandotory为true...."+System.currentTimeMillis();

        //错误发送 mandotory 为true
        channel.basicPublish(exchangeName,errorRoutingKey,true,basicProperties,errorMsg2.getBytes());


    }
    static public class SalkReturnListener implements ReturnListener {
        @Override
        public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
            System.out.println("replyCode:"+replyCode);
            System.out.println("replyText:"+replyText);
            System.out.println("exchange:"+exchange);
            System.out.println("routingKey:"+routingKey);
            System.out.println("properties:"+properties);
            System.out.println("msg body:"+new String(body));

        }
    }

}

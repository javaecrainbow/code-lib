package com.salk.lib.rabbitmq.comfirm_listener;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq confirmi消息投递模式
 * 
 * @author salk
 */
public class ConfirmRabbitmqProducer {

    public static void main(String[] args) throws IOException, TimeoutException {

        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.168.1.36");
        connectionFactory.setPort(5670);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");

        // 创建一个连接
        Connection connection = connectionFactory.newConnection();

        // 创建一个channel
        Channel channel = connection.createChannel();

        // 设置消息投递模式(确认模式)
        channel.confirmSelect();

        // 准备发送消息
        String exchangeName = "salk.confirm.topicexchange";
        String routingKey = "salk.confirm.key";

        // 设置消息属性
        Map<String, Object> tulingInfo = new HashMap<>();
        tulingInfo.put("company", "salk");
        tulingInfo.put("location", "南京");

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2)
            .correlationId(UUID.randomUUID().toString()).timestamp(new Date()).headers(tulingInfo).build();

        String msgContext = "你好 salk....";
        /**
         * 消息确认监听
         */
        channel.addConfirmListener(new SalkConfirmListener());

        channel.basicPublish(exchangeName, routingKey, basicProperties, msgContext.getBytes());

        /**
         * 注意:在这里千万不能调用channel.close不然 消费就不能接受确认了
         */

    }
    static class SalkConfirmListener implements ConfirmListener {
        /**
         *
         * @param deliveryTag 唯一消息Id
         * @param multiple:是否批量
         * @throws IOException
         */
        @Override
        public void handleAck(long deliveryTag, boolean multiple) throws IOException {
            System.out.println("当前时间:"+System.currentTimeMillis()+"salkConfirmListener handleAck:"+deliveryTag);
        }

        /**
         * 处理异常
         * @param deliveryTag
         * @param multiple
         * @throws IOException
         */
        @Override
        public void handleNack(long deliveryTag, boolean multiple) throws IOException {
            System.out.println("salkConfirmListener handleNack:"+deliveryTag);

        }
    }

}

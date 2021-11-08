package com.salk.lib.practice.delaycheck.consumer.component;

import java.io.IOException;

import com.salk.lib.practice.delaycheck.consumer.bo.MsgTxtBo;
import com.salk.lib.practice.delaycheck.consumer.service.IProductService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MqConsumer {

    /**队列名称*/
    public static final String ORDER_TO_PRODUCT_QUEUE_NAME = "order-to-product.queue";

    public static final String LOCK_KEY = "lock.key";

    @Autowired
    private IProductService productService;


    @Autowired
    private MsgSender msgSender;


    @RabbitListener(queues = {ORDER_TO_PRODUCT_QUEUE_NAME})
    @RabbitHandler
    public void consumerMsgWithLock(Message message, Channel channel) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        MsgTxtBo msgTxtBo = objectMapper.readValue(message.getBody(), MsgTxtBo.class);
        Long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // 幂等校验
        boolean flag=false;
        log.info("消费消息:{}", msgTxtBo);

        if(flag){
            log.warn("请不要重复消费消息{}", msgTxtBo);
            channel.basicReject(deliveryTag,false);
            return;
        }
            try {
                //更新消息表也业务表
                productService.updateProductStore(msgTxtBo);

                //发送一条确认消费消息到callback服务上
                msgSender.senderMsg(msgTxtBo);
                //消息签收
                channel.basicAck(deliveryTag,false);
            } catch (Exception e) {

            }

    }
}

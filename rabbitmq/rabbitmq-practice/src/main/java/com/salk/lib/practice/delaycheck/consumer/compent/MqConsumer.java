package com.salk.lib.practice.delaycheck.consumer.compent;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.tuling.bo.MsgTxtBo;
import com.tuling.exception.BizExp;
import com.tuling.service.IProductService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by smlz on 2019/10/13.
 */
@Component
@Slf4j
public class MqConsumer {

    /**队列名称*/
    public static final String ORDER_TO_PRODUCT_QUEUE_NAME = "order-to-product.queue";

    public static final String LOCK_KEY = "lock.key";

    @Autowired
    private IProductService productService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MsgSender msgSender;


    @RabbitListener(queues = {ORDER_TO_PRODUCT_QUEUE_NAME})
    @RabbitHandler
    public void consumerMsgWithLock(Message message, Channel channel) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        MsgTxtBo msgTxtBo = objectMapper.readValue(message.getBody(), MsgTxtBo.class);
        Long deliveryTag = (Long) message.getMessageProperties().getDeliveryTag();

        if (redisTemplate.opsForValue().setIfAbsent(LOCK_KEY + msgTxtBo.getMsgId(), msgTxtBo.getMsgId())) {
            log.info("消费消息:{}", msgTxtBo);
            try {
                //更新消息表也业务表
                productService.updateProductStore(msgTxtBo);

                //发送一条确认消费消息到callback服务上
                msgSender.senderMsg(msgTxtBo);
                //消息签收
                channel.basicAck(deliveryTag,false);
            } catch (Exception e) {

                if (e instanceof BizExp) {
                    BizExp bizExp = (BizExp) e;
                    log.info("数据业务异常:{},即将删除分布式锁", bizExp.getErrMsg());
                    //删除分布式锁
                    redisTemplate.delete(LOCK_KEY);
                }

            }

        } else {
            log.warn("请不要重复消费消息{}", msgTxtBo);
            channel.basicReject(deliveryTag,false);
        }

    }
}

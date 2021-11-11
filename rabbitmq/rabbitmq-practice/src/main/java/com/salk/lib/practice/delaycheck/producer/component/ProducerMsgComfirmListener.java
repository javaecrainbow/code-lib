package com.salk.lib.practice.delaycheck.producer.component;

import com.salk.lib.practice.delaycheck.producer.enumuration.OrderStatusEnum;
import com.salk.lib.practice.delaycheck.producer.mapper.OrderInfoMapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class ProducerMsgComfirmListener implements RabbitTemplate.ConfirmCallback{

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String msgId = correlationData.getId();
        if(ack) {
            log.info("成功消费消息:{}",msgId);
        }else{
            dealMsgNack(msgId);
        }

    }

    /**
     * 删除我们的订单
     */
    private void updateOrderStatus(Long orderNo) {
        orderInfoMapper.updateOrderStatusById(orderNo, OrderStatusEnum.ERROR.getCode());
    }

    private void dealMsgNack(String msgId) {

        //表示是业务消息没有发送到broker中,那么我们需要删除订单(真正的场景是更新订单状态为作废状态)
        if(!msgId.contains("delay")) {
            log.info("发送业务消息失败:{}",msgId);
            long orderNo = Long.parseLong(msgId.split("_")[1]);
            //删除订单
            updateOrderStatus(orderNo);
        }else{
            //检查消息发送失败,那么不会做可靠性检查，需要重新发送消息
            log.info("延时消息没有发送成功:{}",msgId);
        }
    }
}

package com.salk.lib.practice.delaycheck.producer.component;

import com.salk.lib.practice.delaycheck.producer.bo.MsgTxtBo;
import com.salk.lib.practice.delaycheck.producer.constants.MqConst;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;


/**
 * 消息发送组件
 */
@Component
@Slf4j
public class ProducerMsgSender implements InitializingBean {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ProducerMsgComfirmListener msgComfirmListener;


    /**
     * 发送业务消息
     * @param msgTxtBo
     */
    public  void senderMsg(MsgTxtBo msgTxtBo){


        log.info("发送的消息ID:{}",msgTxtBo.getOrderNo());

        CorrelationData correlationData = new CorrelationData(msgTxtBo.getMsgId()+"_"+msgTxtBo.getOrderNo());

        rabbitTemplate.convertAndSend(MqConst.ORDER_TO_PRODUCT_EXCHANGE_NAME,MqConst.ORDER_TO_PRODUCT_ROUTING_KEY,msgTxtBo,correlationData);
    }

    /**
     * 发送延时消息
     * @param msgTxtBo
     */
    public void senderDelayCheckMsg(MsgTxtBo msgTxtBo) {

        log.info("发送的消息ID:{}",msgTxtBo.getOrderNo());
        //表示为延时消息
        CorrelationData correlationData = new CorrelationData(msgTxtBo.getMsgId()+"_"+msgTxtBo.getOrderNo()+"_delay");
        rabbitTemplate.convertAndSend(MqConst.ORDER_TO_PRODUCT_DELAY_EXCHANGE_NAME, MqConst.ORDER_TO_PRODUCT_DELAY_ROUTING_KEY, msgTxtBo, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置延迟时间
                message.getMessageProperties().setHeader("x-delay", MqConst.DELAY_TIME);
                return message;
            }
        },correlationData);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rabbitTemplate.setConfirmCallback(msgComfirmListener);
        //设置消息转换器
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
    }
}

package com.salk.lib.practice.delaycheck.consumer.component;

import com.salk.lib.practice.delaycheck.consumer.bo.MsgTxtBo;
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


@Component
@Slf4j
public class MsgSender implements InitializingBean {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**队列名称*/
    public static final String ORDER_TO_PRODUCT_EXCHANGE_NAME = "order-to-product.exchange";

    public static final String PRODUCT_TO_CALLBACK_ROUTING_KEY = "product_to_callback_key";


    /**
     * 方法实现说明:真正的发送消息
     * @author:smlz
     * @param msgTxtBo:发送的消息对象
     * @return:
     * @exception:
     * @date:2019/10/11 20:01
     */
    public  void senderMsg(MsgTxtBo msgTxtBo){


        log.info("发送的消息ID:{}",msgTxtBo.getOrderNo());

        CorrelationData correlationData = new CorrelationData(msgTxtBo.getMsgId()+"_"+msgTxtBo.getOrderNo());

        rabbitTemplate.convertAndSend(ORDER_TO_PRODUCT_EXCHANGE_NAME,PRODUCT_TO_CALLBACK_ROUTING_KEY,msgTxtBo,correlationData);
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
                message.getMessageProperties().setHeader("x-delay", MqConst.DELAY_TIME);//设置延迟时间
                return message;
            }
        },correlationData);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //设置消息转换器
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
    }
}

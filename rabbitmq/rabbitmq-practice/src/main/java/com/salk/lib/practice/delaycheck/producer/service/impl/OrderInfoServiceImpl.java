package com.salk.lib.practice.delaycheck.producer.service.impl;

import java.util.Date;
import java.util.UUID;

import com.salk.lib.practice.delaycheck.producer.bo.MsgTxtBo;
import com.salk.lib.practice.delaycheck.producer.component.ProducerMsgSender;
import com.salk.lib.practice.delaycheck.producer.constants.MqConst;
import com.salk.lib.practice.delaycheck.producer.entity.MessageContent;
import com.salk.lib.practice.delaycheck.producer.entity.OrderInfo;
import com.salk.lib.practice.delaycheck.producer.enumuration.MsgStatusEnum;
import com.salk.lib.practice.delaycheck.producer.mapper.OrderInfoMapper;
import com.salk.lib.practice.delaycheck.producer.service.IOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;


import lombok.extern.slf4j.Slf4j;

/**
 * @author salk
 */
@Slf4j
@Service
public class OrderInfoServiceImpl implements IOrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ProducerMsgSender msgSender;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrderInfo(OrderInfo orderInfo) {

        try {
            orderInfoMapper.saveOrderInfo(orderInfo);
        }catch (Exception e) {
            log.error("操作数据库失败:{}",e);
            throw new RuntimeException("操作数据库失败");
        }
    }
    @Override
    public void saveOrderInfoWithMessage(OrderInfo orderInfo) throws JsonProcessingException {
        //构建消息对象
        MessageContent messageContent = builderMessageContent(orderInfo.getOrderNo(),orderInfo.getProductNo());
        //保存数据库
        saveOrderInfo(orderInfo);
        //构建消息发送对象
        MsgTxtBo msgTxtBo = new MsgTxtBo();
        msgTxtBo.setMsgId(messageContent.getMsgId());
        msgTxtBo.setOrderNo(orderInfo.getOrderNo());
        msgTxtBo.setProductNo(orderInfo.getProductNo());
        //第一次发送消息
        msgSender.senderMsg(msgTxtBo);
        msgSender.senderDelayCheckMsg(msgTxtBo);
    }


    /**
     * 构建消息体
     * @param orderNo
     * @param productNo
     * @return
     */
    private MessageContent builderMessageContent(long orderNo,Integer productNo) {
        MessageContent messageContent = new MessageContent();
        String msgId = UUID.randomUUID().toString();
        messageContent.setMsgId(msgId);
        messageContent.setCreateTime(new Date());
        messageContent.setUpdateTime(new Date());
        messageContent.setExchange(MqConst.ORDER_TO_PRODUCT_EXCHANGE_NAME);
        messageContent.setRoutingKey(MqConst.ORDER_TO_PRODUCT_QUEUE_NAME);
        messageContent.setMsgStatus(MsgStatusEnum.SENDING.getCode());
        messageContent.setOrderNo(orderNo);
        messageContent.setProductNo(productNo);
        messageContent.setMaxRetry(MqConst.MSG_RETRY_COUNT);
        return messageContent;
    }
}

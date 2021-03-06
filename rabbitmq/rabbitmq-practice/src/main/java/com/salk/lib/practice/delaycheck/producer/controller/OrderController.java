package com.salk.lib.practice.delaycheck.producer.controller;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.salk.lib.practice.delaycheck.producer.bo.MsgTxtBo;
import com.salk.lib.practice.delaycheck.producer.component.ProducerMsgSender;
import com.salk.lib.practice.delaycheck.producer.entity.OrderInfo;
import com.salk.lib.practice.delaycheck.producer.service.IOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class OrderController {

    @Autowired
    private IOrderInfoService orderInfoService;

    @Autowired
    private ProducerMsgSender msgSender;

    @RequestMapping("/saveOrder")
    public String saveOrder() throws JsonProcessingException {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(System.currentTimeMillis());
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        orderInfo.setUserName("salk");
        orderInfo.setMoney(10000);
        orderInfo.setProductNo(1);
        orderInfoService.saveOrderInfoWithMessage(orderInfo);
        return "ok";
    }

    /**
     * 订单重试生成接口
     * @return
     */
    @RequestMapping("/retryMsg")
    public String retryMsg( @RequestBody MsgTxtBo msgTxtBo) {

        log.info("消息重新发送:{}",msgTxtBo);

        //第一次发送消息
        msgSender.senderMsg(msgTxtBo);
        msgSender.senderDelayCheckMsg(msgTxtBo);

        return "ok";
    }
}

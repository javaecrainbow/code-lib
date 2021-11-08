package com.salk.lib.practice.delaycheck.producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.salk.lib.practice.delaycheck.producer.entity.OrderInfo;

/**
 * @author salk
 */
public interface IOrderInfoService {
    /**
     * 订单保存
     * @param orderInfo
     */
    void saveOrderInfo(OrderInfo orderInfo);

    /**
     * 订单保存并发送消息
     * @param orderInfo
     * @throws JsonProcessingException
     */
    void saveOrderInfoWithMessage(OrderInfo orderInfo) throws JsonProcessingException;
}

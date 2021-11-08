package com.salk.lib.practice.delaycheck.producer.mapper;

import com.salk.lib.practice.delaycheck.producer.entity.OrderInfo;

public interface OrderInfoMapper {

    /**
     * 保存订单
     * @param orderInfo
     * @return
     */
    int saveOrderInfo(OrderInfo orderInfo);

    /**
     * 更新订单
     * @param orderNo
     * @param orderStatus
     * @return
     */
    int updateOrderStatusById(Long orderNo, Integer orderStatus);
}

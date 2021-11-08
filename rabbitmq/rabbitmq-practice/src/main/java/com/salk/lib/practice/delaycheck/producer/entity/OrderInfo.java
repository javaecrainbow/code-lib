package com.salk.lib.practice.delaycheck.producer.entity;

import java.util.Date;

import com.salk.lib.practice.delaycheck.producer.enumuration.OrderStatusEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class OrderInfo {

    private long orderNo;

    private Date createTime;

    private Date updateTime;

    private String userName;

    private double money;

    private Integer productNo;

    private Integer orderStatus = OrderStatusEnum.SUCCESS.getCode();
}

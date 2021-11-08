package com.salk.lib.practice.delaycheck.producer.entity;

import java.util.Date;

import com.tuling.enumuration.OrderStatusEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:订单实体
* @author: smlz
* @createDate: 2019/10/11 15:01
* @version: 1.0
*/
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

    private Integer orderStatus= OrderStatusEnum.SUCCESS.getCode();
}

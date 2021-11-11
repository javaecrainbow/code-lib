package com.salk.lib.practice.delaycheck.producer.entity;

import java.util.Date;

import lombok.Data;


@Data
public class MessageContent {

    private String msgId;

    private long orderNo;

    private Date createTime;

    private Date updateTime;

    private Integer msgStatus;

    private String exchange;

    private String routingKey;

    private String errCause;

    private Integer maxRetry;

    private Integer currentRetry=0;

    private Integer productNo;
}

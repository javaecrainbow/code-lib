package com.salk.lib.practice.delaycheck.producer.enumuration;

import lombok.Getter;


@Getter
public enum OrderStatusEnum {

    SUCCESS(0,"订单生成"),

    ERROR(1,"订单作废");


    private Integer code;

    private String msgStatus;



    OrderStatusEnum(Integer code, String msgStatus) {
        this.code = code;
        this.msgStatus = msgStatus;
    }


}

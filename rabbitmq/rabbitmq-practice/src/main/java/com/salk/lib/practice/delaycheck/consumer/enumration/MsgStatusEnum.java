package com.salk.lib.practice.delaycheck.consumer.enumration;

import lombok.Getter;


@Getter
public enum MsgStatusEnum {

    SENDING(0,"发送中"),

    SENDING_SUCCESS(1,"消息发送成功"),

    SENDING_FAIL(2,"消息发送失败"),

    CONSUMER_SUCCESS(3,"消费成功"),

    CONSUMER_FAIL(4,"消费失败");

    private Integer code;

    private String msgStatus;



    MsgStatusEnum(Integer code, String msgStatus) {
        this.code = code;
        this.msgStatus = msgStatus;
    }


}

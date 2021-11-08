package com.salk.lib.practice.delaycheck.producer.bo;

import java.io.Serializable;

import lombok.Data;


@Data
public class MsgTxtBo implements Serializable {

    private long orderNo;

    private int productNo;

    private String msgId;
}

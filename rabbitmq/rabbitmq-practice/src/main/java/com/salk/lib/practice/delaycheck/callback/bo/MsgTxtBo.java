package com.salk.lib.practice.delaycheck.callback.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class MsgTxtBo implements Serializable {

    private long orderNo;

    private int productNo;

    private String msgId;
}

package com.salk.lib.practice.delaycheck.consumer.bo;

import java.io.Serializable;

import lombok.Data;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:消息文本对象
* @author: smlz
* @createDate: 2019/10/11 17:30
* @version: 1.0
*/
@Data
public class MsgTxtBo implements Serializable {

    private long orderNo;

    private int productNo;

    private String msgId;
}

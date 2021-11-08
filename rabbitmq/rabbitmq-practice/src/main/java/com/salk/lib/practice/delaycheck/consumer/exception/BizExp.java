package com.salk.lib.practice.delaycheck.consumer.exception;

import lombok.Data;

/**
 * Created by smlz on 2019/10/14.
 */
@Data
public class BizExp extends RuntimeException {

    private Integer code;

    private String errMsg;

    public BizExp(Integer code,String errMsg) {
        super();
        this.code = code;
        this.errMsg = errMsg;
    }

}

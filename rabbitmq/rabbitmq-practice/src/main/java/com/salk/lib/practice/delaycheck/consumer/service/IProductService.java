package com.salk.lib.practice.delaycheck.consumer.service;


import com.salk.lib.practice.delaycheck.consumer.bo.MsgTxtBo;


public interface IProductService {

    /**
     * 更新库存
     * @param msgTxtBo
     * @return
     */
    boolean updateProductStore(MsgTxtBo msgTxtBo);

}

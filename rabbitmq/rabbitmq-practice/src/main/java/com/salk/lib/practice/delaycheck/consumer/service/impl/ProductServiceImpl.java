package com.salk.lib.practice.delaycheck.consumer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salk.lib.practice.delaycheck.consumer.bo.MsgTxtBo;
import com.salk.lib.practice.delaycheck.consumer.exception.BizExp;
import com.salk.lib.practice.delaycheck.consumer.mapper.ProductInfoMapper;
import com.salk.lib.practice.delaycheck.consumer.service.IProductService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductStore(MsgTxtBo msgTxtBo) {
        boolean updateFlag = true;
        try{
            //更新库存
            productInfoMapper.updateProductStoreById(msgTxtBo.getProductNo());
        }catch (Exception e) {
            log.error("更新数据库失败:{}",e);
            throw new BizExp(0,"更新数据库异常");
        }
        return updateFlag;
    }
}

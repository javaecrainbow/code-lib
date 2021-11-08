package com.salk.lib.practice.delaycheck.consumer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tuling.entity.MessageContent;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:消息内容mapper
* @author: smlz
* @createDate: 2019/10/11 16:14
* @version: 1.0
*/
public interface MsgContentMapper {

    /**
     * 方法实现说明:保存消息
     * @author:smlz
     * @param messageContent:消息对象
     * @return:
     * @date:2019/10/11 16:16
     */
    int saveMsgContent(MessageContent messageContent);

    int updateMsgStatus(MessageContent messageContent);

    List<MessageContent> qryNeedRetryMsg(@Param("msgStatus") Integer status, @Param("timeDiff") Integer timeDiff);

    void updateMsgRetryCount(String msgId);


}

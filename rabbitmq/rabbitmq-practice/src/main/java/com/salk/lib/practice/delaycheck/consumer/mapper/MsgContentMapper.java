package com.salk.lib.practice.delaycheck.consumer.mapper;

import java.util.List;

import com.salk.lib.practice.delaycheck.consumer.entity.MessageContent;
import org.apache.ibatis.annotations.Param;



public interface MsgContentMapper {

    /**
     * 保存消息表，在确认消费后插进
     * @param messageContent
     * @return
     */
    int saveMsgContent(MessageContent messageContent);

    int updateMsgStatus(MessageContent messageContent);

    List<MessageContent> qryNeedRetryMsg(@Param("msgStatus") Integer status, @Param("timeDiff") Integer timeDiff);

    void updateMsgRetryCount(String msgId);


}

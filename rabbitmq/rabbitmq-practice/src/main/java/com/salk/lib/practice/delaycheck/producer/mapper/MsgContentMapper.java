package com.salk.lib.practice.delaycheck.producer.mapper;

import java.util.List;

import com.salk.lib.practice.delaycheck.producer.entity.MessageContent;
import org.apache.ibatis.annotations.Param;

/**
 * 消息保存mapper
 */
public interface MsgContentMapper {


    /**
     * 保存味精内容
     *
     * @param messageContent 消息内容
     * @return int
     */
    int saveMsgContent(MessageContent messageContent);

    /**
     * 消息条件查询
     * @param status
     * @param timeDiff
     * @return
     */
    List<MessageContent> qryNeedRetryMsg(@Param("msgStatus") Integer status, @Param("timeDiff") Integer timeDiff);

    /**
     * 更新重试次数
     * @param msgId
     */
    void updateMsgRetryCount(String msgId);
}

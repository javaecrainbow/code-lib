package com.salk.lib.practice.delaycheck.callback.mapper;

import com.salk.lib.practice.delaycheck.callback.entity.MessageContent;

public interface MsgContentMapper {


    int saveMsgContent(MessageContent messageContent);

    MessageContent qryMessageContentById(String msgId);

}

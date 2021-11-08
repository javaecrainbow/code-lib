package com.salk.lib.rabbitmq.spring.messagedelegate;


import com.salk.lib.rabbitmq.spring.entity.Order;

import java.io.File;
import java.util.Map;

/**
 * Created by smlz on 2019/10/8.
 */
public class SalkMsgDelegate {


    public void handleMessage(String msgBody) {
        System.out.println("SalkMsgDelegate。。。。。。handleMessage"+msgBody);
    }

    public void consumerMsg(String msg){
        System.out.println("SalkMsgDelegate。。。。。。consumerMsg"+msg);
    }

    public void consumerTopicQueue(String msgBody) {
        System.out.println("SalkMsgDelegate。。。。。。consumerTopicQueue"+msgBody);

    }

    public void consumerTopicQueue2(String msgBody) {
        System.out.println("SalkMsgDelegate。。。。。。consumerTopicQueue2"+msgBody);

    }

    /**
     * 处理json
     * @param jsonMap
     */
    public void consumerJsonMessage(Map jsonMap) {
        System.out.println("SalkMsgDelegate ============================处理json"+jsonMap);
    }

    /**
     * 处理order得
     * @param order
     */
    public void consumerJavaObjMessage(Order order) {
        System.out.println("SalkMsgDelegate ============================处理java对象"+order.toString());

    }



    public void consumerFileMessage(File file) {
        System.out.println("SalkMsgDelegate========================处理文件"+file.getName());
    }
}

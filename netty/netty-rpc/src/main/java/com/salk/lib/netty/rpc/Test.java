package com.salk.lib.netty.rpc;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author salkli
 * @since 2021/12/27
 **/
public class Test {
    public static void main(String[] args) {
        test2();
    }
    public static void test1(){
        String s2="\\";
        String s = StringEscapeUtils.escapeJava(s2);
        System.out.println(s);
        String ss="{\"key1\":\""+s+"\"}";
        System.out.println(ss);
        JSONObject parse = JSONObject.parseObject(ss);
        System.out.println(parse.getString("key1"));
    }
    public static void test2(){
        Map<String,String> ss=new HashMap();
        String value="\\";
        ss.put("key1",value);
        String s1 = JSONObject.toJSONString(ss);
        String s = StringEscapeUtils.escapeJava(s1);
        JSONObject parse = JSONObject.parseObject(s);
        System.out.println(parse.getString("key1"));
    }
}

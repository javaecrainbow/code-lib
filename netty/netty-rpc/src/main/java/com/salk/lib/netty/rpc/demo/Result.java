package com.salk.lib.netty.rpc.demo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by 18073747 on 2018/7/17.
 */
public class Result implements Serializable{
    private String value;
    private static final String s="2";
    private Integer length;
    private transient String s5="555";
    private List<Map<String,String>> content;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<Map<String, String>> getContent() {
        return content;
    }

    public void setContent(List<Map<String, String>> content) {
        this.content = content;
    }
}

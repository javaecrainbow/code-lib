package com.salk.lib.linktable;

/**
 * @author salkli
 * @since 2022/9/26
 **/
public class MyNode {
    private MyNode next;
    private Integer value;

    public MyNode(Integer value) {
        this.value = value;
        this.next = null;
    }

    public MyNode(MyNode next, Integer value) {
        this.next = next;
        this.value = value;
    }

    public MyNode getNext() {
        return next;
    }

    public void setNext(MyNode next) {
        this.next = next;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}

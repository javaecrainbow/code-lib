package com.salk.lib.demo.genericity;

import java.util.Date;

/**
 * @author salkli
 * @since 2022/9/2
 **/
public class Pair<T> {
    private T t;

    public void setData(T t){
        this.t=t;
    }

    public T getData(){
        return t ;
    }
}

class Demo extends Pair<Date> {
    private Date t;

    @Override
    public Date getData() {
        return t;
    }

    @Override
    public void setData(Date date) {
        this.t = date;
    }
}

package com.salk.lib.netty.rpc.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 18073747 on 2018/7/17.
 */
public class TestImpl implements TestIntf {
    @Override
    public Result hello(String word) {
        System.out.println("test hello========"+word);
        Result result=new Result();
        result.setLength(100);
        result.setValue(word);
        List<Map<String,String>> contents=new ArrayList();
        contents.add(new HashMap());
        result.setContent(contents);
        return result;
    }
}

package com.salk.lib.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author salkli
 * @since 2022/2/11
 **/
public class MapTest {
    private static Map<String, List<String>> content=new HashMap();

    public static void main(String[] args) {
        init2("1","2");
        init2("1","1");
        init2("2","1");
        init2("2","2");
        System.out.println(content);

    }

    private static void init(String key,String value){
        List<String> strings = content.computeIfAbsent(key, v -> new ArrayList());
        strings.add(value);
    }

    private static void init2(String key,String value){
        List<String> ss=new ArrayList();
        List<String> strings = content.putIfAbsent(key,ss );
        strings.add(value);
    }
}

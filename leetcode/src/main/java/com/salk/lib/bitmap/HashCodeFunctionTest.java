package com.salk.lib.bitmap;


import cn.hutool.core.util.HashUtil;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;

/**
 * @author salkli
 * @since 2023/3/27
 **/
public class HashCodeFunctionTest {


    @Test
    public static  void testSHA3(){
        HashMap s=new HashMap(16);
        s.put("123","123");
        //long l = HashUtil.mixHash("123");

    }

    @Test
    public static void test2()throws Exception{
        String ss="如果 C=0 说明完全匹配\n" + "否则 C|A =A 则是漏选了即 A 包含了B\n" + "如果C|A !=A 则是选错了即 A不包含B\n" + "如果要看C 哪些是选对了哪些是选错了则和a ，b,c,d,e 分别取&";
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] hashbytes = digest.digest(
                ss.getBytes(StandardCharsets.UTF_8));
        String sha3Hex = bytesToHex(hashbytes);
        System.out.println(sha3Hex);
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    @Test
    public static  void testWei(){
        long longs = HashUtil.murmur64("123".getBytes());
        String s = Long.toBinaryString(longs);
        System.out.println(s);
        long longs2 = HashUtil.murmur64("123".getBytes());
        String s2 = Long.toBinaryString(longs);
        System.out.println(s2);
        System.out.println((longs^longs2)==0);
    }


}

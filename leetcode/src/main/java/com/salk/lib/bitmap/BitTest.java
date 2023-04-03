package com.salk.lib.bitmap;

import cn.hutool.core.util.HashUtil;
import com.googlecode.javaewah.EWAHCompressedBitmap;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author salkli
 * @since 2023/3/23
 **/
public class BitTest {
    public static void main(String[] args) {
        //test3();
        test345();
    }

    @Test
    public void test_03() throws IOException {
        EWAHCompressedBitmap ewahBitmap1 = EWAHCompressedBitmap.bitmapOf(1, 2);
        EWAHCompressedBitmap ewahBitmap2 = EWAHCompressedBitmap.bitmapOf(1, 3);
        System.out.println("bitmap 1: " + ewahBitmap1);
        System.out.println("bitmap 2: " + ewahBitmap2);

        // or
        EWAHCompressedBitmap orbitmap = ewahBitmap1.or(ewahBitmap2);
        System.out.println("bitmap 1 OR bitmap 2: " + orbitmap);
        System.out.println("memory usage: " + orbitmap.sizeInBytes() + " bytes");

        // and
        EWAHCompressedBitmap andbitmap = ewahBitmap1.and(ewahBitmap2);
        System.out.println("bitmap 1 AND bitmap 2: " + andbitmap);
        System.out.println("memory usage: " + andbitmap.sizeInBytes() + " bytes");

        // xor
        EWAHCompressedBitmap xorbitmap = ewahBitmap1.xor(ewahBitmap2);
        System.out.println("bitmap 1 XOR bitmap 2:" + xorbitmap);
        System.out.println("memory usage: " + xorbitmap.sizeInBytes() + " bytes");

        // fast aggregation over many bitmaps
        EWAHCompressedBitmap ewahBitmap3 = EWAHCompressedBitmap.bitmapOf(5, 55, 1 << 30);
        EWAHCompressedBitmap ewahBitmap4 = EWAHCompressedBitmap.bitmapOf(4, 66, 1 << 30);
        System.out.println("bitmap 3: " + ewahBitmap3);
        System.out.println("bitmap 4: " + ewahBitmap4);

        andbitmap = EWAHCompressedBitmap.and(ewahBitmap1, ewahBitmap2, ewahBitmap3, ewahBitmap4);
        System.out.println("b1 AND b2 AND b3 AND b4: " + andbitmap);

        // serialization
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // Note: you could use a file output steam instead of ByteArrayOutputStream
        ewahBitmap1.serialize(new DataOutputStream(bos));
        EWAHCompressedBitmap ewahBitmap1new = EWAHCompressedBitmap.bitmapOf(1,2);
        byte[] bout = bos.toByteArray();
        ewahBitmap1new.deserialize(new DataInputStream(new ByteArrayInputStream(bout)));
        System.out.println("bitmap 1 (recovered) : " + ewahBitmap1new);
        if (!ewahBitmap1.equals(ewahBitmap1new)) throw new RuntimeException("Will not happen");
        //
        // we can use a ByteBuffer as backend for a bitmap
        // which allows memory-mapped bitmaps
        //
        ByteBuffer bb = ByteBuffer.wrap(bout);
        EWAHCompressedBitmap rmap = new EWAHCompressedBitmap(bb);
        System.out.println("bitmap 1 (mapped) : " + rmap);

        if (!rmap.equals(ewahBitmap1)) throw new RuntimeException("Will not happen");
        //
        // support for threshold function (new as of version 0.8.0):
        // mark as true a bit that occurs at least T times in the source
        // bitmaps
        //
        EWAHCompressedBitmap threshold2 = EWAHCompressedBitmap.threshold(2,
                ewahBitmap1, ewahBitmap2, ewahBitmap3, ewahBitmap4);
        System.out.println("threshold 2 : " + threshold2);
    }


    @Test
    public static void ewahCompressBitMap()throws Exception{
        EWAHCompressedBitmap integers = EWAHCompressedBitmap.bitmapOf(1, 2);
        EWAHCompressedBitmap integers1 = EWAHCompressedBitmap.bitmapOf(1, 3);
        //8589934592 12
        EWAHCompressedBitmap xor = integers.xor(integers1);
        System.out.println(xor);
        EWAHCompressedBitmap or = xor.or(integers);
        System.out.println("是否包含:"+(or.cardinality()==integers1.cardinality()?"是":"否"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // Note: you could use a file output steam instead of ByteArrayOutputStream
        or.serialize(new DataOutputStream(bos));
        EWAHCompressedBitmap ewahBitmap1new = new EWAHCompressedBitmap();
        byte[] bout = bos.toByteArray();
        ewahBitmap1new.deserialize(new DataInputStream(new ByteArrayInputStream(bout)));

        System.out.println(or.equals(integers));
    }
    @Test
    public static void test10(){
        StrToBitMap strToBitMap=new StrToBitMap("1");
        strToBitMap.addBitMap("2");
        Long longMax = strToBitMap.getBit().get(0);
        StrToBitMap strToBitMap2=new StrToBitMap("1");
        strToBitMap2.addBitMap("3");
        Long longMax2 = strToBitMap2.getBit().get(0);

        BitSet bitSet=new BitSet(64);
        long l1 = 8589934592L; //001
        System.out.println("l1:"+Long.toBinaryString(l1));
        long l2 = 6L; //010
        System.out.println("l2:"+Long.toBinaryString(l2));
//        long l3 = 3L;011
        long X1 = l1 | l2 ; //011
        System.out.println(X1);
        System.out.println("X1:"+Long.toBinaryString(X1));

//        long ll1 = 1L;
        long ll2 = 8589934592L; //010
        System.out.println("ll2:"+Long.toBinaryString(ll2));
        long ll3 = 12L; //011
        System.out.println("ll3:"+Long.toBinaryString(ll3));
        long X2 = ll3 | ll2;//011
        System.out.println(X2);
        System.out.println("X2:"+Long.toBinaryString(X2));

        long yihuo = X1 ^ X2;
        System.out.println("yihuo:"+Long.toBinaryString(yihuo));
        System.out.println(yihuo);
        System.out.println(yihuo == 0 ? "相同" : "不相同");
        System.out.println((yihuo | X1) == X1 ? "包含" : "不包含");
    }

    @Test
    public static void test1(){
        long l1=320102L;//玄武
        long l2=320104L;//秦淮
        long ll12 = l1 | l2;
        System.out.println(ll12);

        long ll1=320104L;//秦淮
        long ll2=320112L;//浦口
        long  llll12= ll1 | ll2;
        System.out.println(llll12);

        long yihuo=ll12 ^llll12;
        System.out.println(yihuo);

        System.out.println(yihuo==0?"相同":"不相同");

        System.out.println((yihuo|ll12)==ll12 ?"包含":"不包含");

        //long  a= Long.MAX_VALUE;
        //long  b= Long.MAX_VALUE;
        //System.out.println(a|b);
    }


    @Test(invocationCount = 50)
    public static void test4(){
        String l1="320102";
        String l2="320104";
        String l3="320105";
        long hash1= HashUtil.cityHash64(l1.getBytes());
        long hash2= HashUtil.cityHash64(l2.getBytes());
        long hash3= HashUtil.cityHash64(l3.getBytes());

        long l12 = hash1 | hash2|hash3;
        System.out.println(l12);
        String n1="320104";
        String n2="320102";
        long hashn1= HashUtil.cityHash64(n1.getBytes());
        long hashn2= HashUtil.cityHash64(n2.getBytes());
        long n12 = hashn1 | hashn2;
        System.out.println(n12);

        long yihuo=l12 ^n12;
        System.out.println(yihuo);
        System.out.println(yihuo==0?"相同":"不相同");

        System.out.println((yihuo|l12)==l12 ?"包含":"不包含");

        long  a= Long.MAX_VALUE;
        long  b= Long.MAX_VALUE;
        System.out.println(a|b);
    }


    public static void test345(){
        List<String> result=new ArrayList();
        result.add("32");
        result.add("3201");
        result.add("320102");
        result.add("320104");
        result.add("320105");
        result.add("320106");
        result.add("320111");
        result.add("320113");
        result.add("320114");
        result.add("320115");
        result.add("320116");
        result.add("320117");
        result.add("320118");
        result.add("3202");
        result.add("320205");
        result.add("320206");
        result.add("320211");
        result.add("320213");
        result.add("320214");
        result.add("320281");
        result.add("320282");
        result.add("3203");
        result.add("320302");
        result.add("320303");
        result.add("320305");
        result.add("320311");
        result.add("320312");
        result.add("320321");
        result.add("320322");
        result.add("320324");
        result.add("320381");
        result.add("320382");
        result.add("3204");
        result.add("320402");
        result.add("320404");
        result.add("320411");
        result.add("320412");
        result.add("320413");
        result.add("320481");
        result.add("3205");
        result.add("320505");
        result.add("320506");
        result.add("320507");
        result.add("320508");
        result.add("320509");
        result.add("320581");
        result.add("320582");
        result.add("320583");
        result.add("320585");
        result.add("3206");
        result.add("320612");
        result.add("320613");
        result.add("320614");
        result.add("320623");
        result.add("320681");
        result.add("320682");
        result.add("320685");
        result.add("3207");
        result.add("320703");
        result.add("320706");
        result.add("320707");
        result.add("320722");
        result.add("320723");
        result.add("320724");
        result.add("3208");
        result.add("320803");
        result.add("320804");
        result.add("320812");
        result.add("320813");
        result.add("320826");
        result.add("320830");
        result.add("320831");
        result.add("3209");
        result.add("320902");
        result.add("320903");
        result.add("320904");
        result.add("320921");
        result.add("320922");
        result.add("320923");
        result.add("320924");
        result.add("320925");
        result.add("320981");
        result.add("3210");
        result.add("321002");
        result.add("321003");
        result.add("321012");
        result.add("321023");
        result.add("321081");
        result.add("321084");
        result.add("3211");
        result.add("321102");
        result.add("321111");
        result.add("321112");
        result.add("321181");
        result.add("321182");
        result.add("321183");
        result.add("3212");
        result.add("321202");
        result.add("321203");
        result.add("321204");
        result.add("321281");
        result.add("321282");
        result.add("321283");
        result.add("3213");
        result.add("321302");
        result.add("321311");
        result.add("321322");
        result.add("321323");
        result.add("321324");

        int l = calculateOR(result, 0);
        System.out.println(l);

    }

    private static int calculateOR(List<String> children, int result) {
        for(String str:children){
            result=result|HashUtil.murmur32(str.getBytes(StandardCharsets.UTF_8));
        }
        return result;
    }

    public static void test3() {
        long l11 = 32L;
        long l12 = 3201 | 3202 | 3203 | 3204 | 3205 | 3206 | 3207 | 3208 | 3209 | 3210 | 3211 | 3212 | 3213;
        long l13 = 320102 | 320104 | 320105 | 320106 | 320111 | 320113 | 320114 | 320115 | 320116 | 320117 | 320118
            | 320205 | 320206 | 320211 | 320213 | 320214 | 320281 | 320282 | 320302 | 320303 | 320305 | 320311 | 320312
            | 320321 | 320322 | 320324 | 320381 | 320382 | 320402 | 320404 | 320411 | 320412 | 320413 | 320481 | 320505
            | 320506 | 320507 | 320508 | 320509 | 320581 | 320582 | 320583 | 320585 | 320612 | 320613 | 320614 | 320623
            | 320681 | 320682 | 320685 | 320703 | 320706 | 320707 | 320722 | 320723 | 320724 | 320803 | 320804 | 320812
            | 320813 | 320826 | 320830 | 320831 | 320902 | 320903 | 320904 | 320921 | 320922 | 320923 | 320924 | 320925
            | 320981 | 321002 | 321003 | 321012 | 321023 | 321081 | 321084 | 321102 | 321111 | 321112 | 321181 | 321182
            | 321183 | 321202 | 321203 | 321204 | 321281 | 321282 | 321283 | 321302 | 321311 | 321322 | 321323 | 321324;
        long l21 = 33L;
        long l22 = 3301L;
        long l23 = 330102 | 330105 | 330106 | 330108 | 330109 | 330110 | 330111 | 330112 | 330113 | 330114 | 330122
            | 330127 | 330182;
        long ll = l11 | l12 | l13 | l21 | l22 | l23;

        // 全量
        // 32 0 0 long l1=32L;//江苏
        // 33 3301 0 long l2=3201L;//杭州
        System.out.println(ll);

        long n11 = 32L;
        long n12 = 3201L;
        long n13 = 320104L;
        long n21 = 32L;
        long n22 = 3201L;
        long n23 = 320111L;

        // long ll1=320104L;//秦淮
        // long ll2=320111L;//浦口
        // 32 3201 320104
        // 32 3201 320111
        long nn = n11 | n12 | n13 | n21 | n22 | n23;
        System.out.println(nn);

        long yihuo = ll ^ nn;
        System.out.println(yihuo);

        System.out.println(yihuo == 0 ? "相同" : "不相同");

        System.out.println((yihuo | ll) == ll ? "包含" : "不包含");

        long a = Long.MAX_VALUE;
        long b = Long.MAX_VALUE;
        System.out.println(a | b);
    }


    public static void test2(){
        String s1="agree";
        String s2="disagree";
        System.out.println(s1.hashCode());
        System.out.println(s2.hashCode());

    }

}

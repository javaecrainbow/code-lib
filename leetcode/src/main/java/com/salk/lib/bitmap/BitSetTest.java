package com.salk.lib.bitmap;

import java.util.Arrays;
import java.util.BitSet;

/**
 * @author salkli
 * @since 2023/3/23
 **/
public class BitSetTest {
    public static void main(String[] args) {
        int[] arrs=new int[]{31,32};
        int[] arrs2=new int[]{32,33};
        int asInt = Arrays.stream(arrs).max().getAsInt();
        System.out.println(asInt);
        BitSet bitSet=new BitSet(asInt+1);
        bitSet.set(31);
        bitSet.set(32);
        //bitSet.set(33);
        //6442450944
        //12884901888

        System.out.println(bitSet);
        int asInt2 = Arrays.stream(arrs2).max().getAsInt();
        System.out.println(asInt2);
        BitSet bitSet2=new BitSet(asInt2+1);
        bitSet2.set(32);
        bitSet2.set(33);
        bitSet.xor(bitSet2);
        System.out.println(bitSet.equals(bitSet2));
        System.out.println(bitSet);

    }
}

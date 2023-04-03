package com.salk.lib.bitmap;

import java.util.EnumSet;

/**
 * @author salkli
 * @since 2023/4/3
 **/
public class BitUtil {

    public static void main(String[] args) {
        long l1=1000L;
        byte[] bytes = longToBytes(l1);
        System.out.println(bytes);
        long result = bytesToLong(bytes);
        System.out.println(result);

    }


    public static byte[] longToBytes(long value) {
        //一个long类型是8个字节
        byte[] buffer = new byte[8]; //
        for (int i = 7; i >= 0; i--) {
            // 取最低8位
            buffer[i] = (byte) (value & 0xff);
            // 右移8位
            value >>= 8;
        }
        return buffer;
    }

    public static long bytesToLong(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            // 左移8位，相当于乘以256
            value <<= 8;
            // 相当于加上一个字节的值
            value += (bytes[i] & 0xff);
        }
        return value;
    }

    /**
     * 将位图转换为字节数组
     * 
     * @param longs
     * @return
     */
    public static byte[] toByteArray(long[] longs) {
        byte[] bytes = new byte[longs.length * 8];
        for (int i = 0; i < longs.length; i++) {
            for (int j = 0; j < 8; j++) {
                bytes[i * 8 + j] = (byte)(longs[i] >> (j * 8));
            }
        }
        return bytes;
    }

    /**
     * 将字节数组转换为位图
     * 
     * @param bytes
     * @return
     */
    public static long[] toLongArray(byte[] bytes) {
        long[] longs = new long[bytes.length / 8];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = 0;
            for (int j = 0; j < 8; j++) {
                longs[i] |= ((long)(bytes[i * 8 + j] & 0xff)) << (j * 8);
            }
        }
        return longs;
    }

    /**
     * 将字节数组转换为对应的枚举类型成员
     * 
     * @param bitmap
     * @param enumClass
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> EnumSet<E> toEnumSet(long[] bitmap, Class<E> enumClass) {
        EnumSet<E> enumSet = EnumSet.noneOf(enumClass);
        for (int i = 0; i < bitmap.length; i++) {
            long word = bitmap[i];
            for (int j = 0; j < 64 && (word >> j) != 0; j++) {
                if ((word & (1L << j)) != 0) {
                    enumSet.add(enumClass.getEnumConstants()[i * 64 + j]);
                }
            }
        }
        return enumSet;
    }

    /**
     * 将字节数组转换为长整型
     * 
     * @param bytes
     * @return
     */
    public static long byteArrayToLong(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long)(bytes[i] & 0xff)) << (i * 8);
        }
        return value;
    }

    public static <E extends Enum<E>> long[] mapEnumToBits(E[] enums) {
        int num_enums = enums.length;
        int bit_per_part = Long.SIZE; // 每个长整型都有 64 位
        int num_parts = (num_enums + bit_per_part - 1) / bit_per_part;
        long[] longs = new long[num_parts];

        for (int i = 0; i < num_enums; i++) {
            E e = enums[i];
            int i_part = i / bit_per_part;
            int i_bit = i % bit_per_part;
            longs[i_part] |= (1L << i_bit);
        }

        return longs;
    }

    public static <E> long[] strToBits(E[] strs) {
        int nums = strs.length;
        int bit_per_part = Long.SIZE; // 每个长整型都有 64 位
        int num_parts = (nums + bit_per_part - 1) / bit_per_part;
        long[] longs = new long[num_parts];

        for (int i = 0; i < nums; i++) {
            E e = strs[i];
            int i_part = i / bit_per_part;
            int i_bit = i % bit_per_part;
            longs[i_part] |= (1L << i_bit);
        }
        return longs;
    }
}

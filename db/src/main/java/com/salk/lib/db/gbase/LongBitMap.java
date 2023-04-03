package com.salk.lib.db.gbase;

import com.salk.lib.db.mysql.bit.BitUtil;

/**
 * @author salkli
 * @since 2023/3/30
 **/
public class LongBitMap {

    public static void main(String[] args) {
        LongBitMap bitMap = new LongBitMap(100);	//10亿
        bitMap.add(2);
        bitMap.add(4);
        bitMap.add(65);
        bitMap.add(66);
        bitMap.add(99);
        bitMap.add(100);
        byte[] bits = bitMap.bits;
        long l = BitUtil.byteArrayToLong(bits);
        System.out.println("long========"+l);

        long[] longs = BitUtil.toLongArray(bits);
        System.out.println("longs========"+longs);

        LongBitMap bitMap2 = new LongBitMap(100);	//10亿
        bitMap2.add(2);
        bitMap2.add(4);
        bitMap2.add(65);
        bitMap2.add(66);
        bitMap2.add(99);
        bitMap2.add(100);
        byte[] bits1 = bitMap2.bits;
        byte[] yihuo=new byte[bits.length];
        for (int i = 0; i < Math.min(bits.length, bits1.length); i++) {
            yihuo[i]=(byte)(bits[i] ^ bits1[i]);

        }
        boolean result=true;
        for (int i = 0; i < Math.min(bits.length, yihuo.length); i++) {
            if((yihuo[i]|bits[i])!=bits[i]){
                result=false;
            }

        }

        System.out.println(result);
    }

    byte[] bits;		//如果是byte那就一个只能存8个数,一个字节是8位
    int max;			//表示最大的那个数

    public LongBitMap(int max) {
        this.max = max;
        bits = new byte[(max >> 3) + 1];		//max/8 + 1
    }
    public void add(int n) {		//往bitmap里面添加数字
        int bitsIndex = n >> 3;		// 除以8 就可以知道在那个byte
        int loc = n & 7;		///这里其实还可以用&运算
        //接下来就是要把bit数组里面的 bisIndex这个下标的byte里面的 第loc 个bit位置为1
        bits[bitsIndex] |= 1 << loc; //或运算
    }
    public void delete(int n){
        int bitsIndex = n >> 3;
        int loc = n & 7;
        bits[bitsIndex] ^= 1 << (loc);//异或运算
    }
    public boolean find(int n) {
        int bitsIndex = n >> 3;		// 除以8 就可以知道在那个byte
        int loc = n & 7;		///这里其实还可以用求余运算,等同于%8
        int flag = bits[bitsIndex] & (1 << loc);	//如果原来的那个位置是0 那肯定就是0 只有那个位置是1 才行
        if(flag == 0) return false;
        return true;
    }


}

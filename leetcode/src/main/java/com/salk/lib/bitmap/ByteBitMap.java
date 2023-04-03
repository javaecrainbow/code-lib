package com.salk.lib.bitmap;

/**
 * @author salkli
 * @since 2023/3/30
 **/
public class ByteBitMap {

    public static void main(String[] args) {
        ByteBitMap bitMap = new ByteBitMap(1000);
        bitMap.add(63);
        //bitMap.add(3);
        //bitMap.add(7);
        //bitMap.add(64);
        //bitMap.add(456);
        //bitMap.add(32333);
        byte[] bits = bitMap.bits;
        long l = BitUtil.bytesToLong(bits);
        System.out.println(l);

        ByteBitMap bitMap2 = new ByteBitMap(8);
        bitMap2.add(2);
        bitMap2.add(3);
        //bitMap2.add(5);
        bitMap2.add(7);
        //bitMap2.add(64);
        //bitMap2.add(456);
        //bitMap2.add(32332);
        byte[] bits1 = bitMap2.bits;

        long l2 = BitUtil.bytesToLong(bits1);
        System.out.println(l2);

        System.out.println("是否包含========"+((l^l2|l)==l));

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

    public ByteBitMap(int max) {
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

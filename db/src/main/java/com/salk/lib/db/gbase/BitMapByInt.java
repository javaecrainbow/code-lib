package com.salk.lib.db.gbase;

public class BitMapByInt {

	int[] data;
	int max;			//表示最大的那个数

	public BitMapByInt(int max) {
		this.max = max;
		data = new int[(max >> 5) + 1];		//max/32 + 1
	}	
	public void add(int n) {		//往bitmap里面添加数字
		int index = n >> 5;		// 除以32 就可以知道在那个int
		int loc = n & 31;		///这里其实还可以用&运算
		//接下来就是要把数组里面的 index这个下标的int里面的 第loc 个bit位置为1
		data[index] |= 1 << loc; //或运算
	}
	public void delete(int n){
		int index = n >> 5;
		int loc = n & 31;
		data[index] ^= 1 << (loc);//异或运算
	}
	public boolean find(int n) {
		int index = n >> 5;		// 除以8 就可以知道在那个byte
		int loc = n & 31;		///这里其实还可以用&运算,等同于%32
		int flag = data[index] & (1 << loc);	//如果原来的那个位置是0 那肯定就是0 只有那个位置是1 才行
		if(flag == 0) return false;
		return true;
	}
	public static void main(String[] args) {
		BitMapByInt map = new BitMapByInt(200000001);	//10亿
		map.add(2);
		map.add(3);
		map.add(65);
		map.add(66);
		map.delete(2);
		System.out.println(map.find(2));
		System.out.println(map.find(3));
	}
}

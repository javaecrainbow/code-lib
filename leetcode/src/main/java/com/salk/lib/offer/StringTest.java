package com.salk.lib.offer;

/**
 * @author salkli
 * @since 2022/10/14
 **/
public class StringTest {

    public static void test() {
        String s = "212 323 323 ";
        char[] chars = s.toCharArray();
        int extendNum = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                extendNum++;
            }
        }
        char[] newValue = new char[s.length() + extendNum * 2];
        int newPos = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                newValue[newPos] = '%';
                newValue[newPos + 1] = '2';
                newValue[newPos + 2] = '0';
                newPos = newPos + 2;
            } else {
                newValue[newPos] = c;
            }
            newPos++;
        }
        System.out.println(new String(newValue));
    }

    public static String reverseLeftWords(String s, int n) {
        char[] chas = s.toCharArray();
        char[] newChar = new char[chas.length];
        int newCharPos = 0;
        for (int i = n; i < chas.length; i++) {
            newChar[newCharPos++] = chas[i];
        }
        for (int i = 0; i < n; i++) {
            newChar[newCharPos++] = chas[i];
        }
        return new String(newChar);
    }

    public static void main(String[] args) {
        //test();
        //String abcdefg = reverseLeftWords("abcdefg", 2);
        int[] inputs = new int[]{2,3,7,8,8,10};
        int target=8;
        System.out.println(sortedHalfSearch(inputs,target));

    }

    public static int sortedHalfSearch(int[] inputs,  int target) {
        int i=0;
        int j=inputs.length-1;
        while(i<=j) {
            int m = (j+i) / 2;
            int value = inputs[m];
            if (value <= target) {
                i = m + 1;
            } else  {
                j = m - 1;
            }
        }
        int right=i;
        i=0;
        j=inputs.length-1;
        while(i<=j) {
            int m = (j+i) / 2;
            int value = inputs[m];
            if (value < target) {
                i = m + 1;
            } else  {
                j = m - 1;
            }
        }
        int left=j;
        return right-left-1;
    }

}

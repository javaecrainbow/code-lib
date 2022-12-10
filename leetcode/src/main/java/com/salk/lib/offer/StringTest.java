package com.salk.lib.offer;

import java.util.ArrayList;
import java.util.List;

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
        //System.out.println(sortedHalfSearch(inputs,target));
        inputs = new int[]{3,3,3,1};
        System.out.println('c'-'a');
        String s="acdeface";
        char c = firstUniqChar(s);
        System.out.println(c);
        //missingNumber(inputs);
        //minArray(inputs);
    }

    public static int sortedHalfSearch(int[] inputs,  int target) {
        int i = 0;
        int j = inputs.length - 1;
        while (i <= j) {
            int m = (j + i) / 2;
            int value = inputs[m];
            if (value <= target) {
                i = m + 1;
            } else {
                j = m - 1;
            }
        }
        int right = i;
        i = 0;
        j = inputs.length - 1;
        while (i <= j) {
            int m = (j + i) / 2;
            int value = inputs[m];
            if (value < target) {
                i = m + 1;
            } else {
                j = m - 1;
            }
        }
        int left = j;
        return right - left - 1;
    }

    public static int missingNumber(int[] nums) {
        int i=0;
        int j=nums.length-1;
        while(i<=j){
            int m=(i+j)/2;
            if(nums[m]!=m){
                j=m-1;
            }else{
                i=m+1;
            }
        }
        return nums[j]-1;
    }
    public static int minArray(int[] numbers) {
        int i=0;
        int j=numbers.length-1;
        while(i<=j){
            int m=(i+j)/2;
            if(numbers[m]>numbers[j]){
                i=m+1;
            }else if(numbers[m]<numbers[j]){
                j=m;
            }else{
                j--;
            }
        }
        return numbers[i];
    }

    public static char firstUniqChar(String s) {
        int sta = 0;
        int vis = 0;
        char[] chars = s.toCharArray();
        for (char c : chars) {
            int x = c - 'a';
            if ((vis & (1 << x)) != 0) {
                continue;
            }
            if ((sta & (1 << x)) != 0) {
                sta ^= (1 << x);
                vis |= (1 << x);

            } else {
                sta |= (1 << x);
            }
        }
            for (char cr : chars) {
                int x2 = cr - 'a';
                if ((sta & (1 << x2)) != 0) {
                    return cr;
                }
            }
            return ' ';
    }


}

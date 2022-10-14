package com.salk.lib.search;

/**
 * 二分查找，时间复杂度 O(logn)
 * @author salkli
 * @since 2021/7/6
 **/
public class BSearch {
    public static void main(String[] args) {
        int[] input = {1, 2, 4, 6, 7, 9, 10};
        int n = input.length;
        int value = 9;
        System.out.println(bSearch(input, n, 9));
    }

    public static int bSearch(int[] param, int n, int value) {
        int low = 0;
        int high = n - 1;
        int searchTime = 0;
        while (low <= high) {
            searchTime++;
            int middle = low+((high-low)>>1);
            if (param[middle] == value) {
                System.out.println("查找次数" + searchTime);
                return middle;
            } else if (param[middle] < value) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }
        System.out.println("查找次数" + searchTime);
        return -1;
    }
}

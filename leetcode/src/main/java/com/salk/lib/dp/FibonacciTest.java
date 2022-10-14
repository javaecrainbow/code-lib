package com.salk.lib.dp;

/**
 *
 * f(n)=f(n-1)+f(n-2)
 * @author salkli
 * @since 2022/7/4
 **/
public class FibonacciTest {
    public static void main(String[] args) {
        //f(0)=1 && f(1)=1
        System.out.println(FibonacciTest.f1(6));
    }

    public static Integer f1(Integer num){
        if(num<=1){
            return num;
        }else {
            return f1(num - 1) + f1(num - 2);
        }

    }
}

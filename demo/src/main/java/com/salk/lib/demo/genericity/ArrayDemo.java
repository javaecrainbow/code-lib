package com.salk.lib.demo.genericity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author salkli
 * @since 2022/7/26
 **/
public class ArrayDemo {

    public static void main(String[] args) {
        // 编译错误，非法创建
        // List<String>[] list1 = new ArrayList<String>[10];
        // OK，但是会有警告
        List<String>[] list2 = (List<String>[])new ArrayList<?>[10];
        // OK，但是会有警告
        List<String>[] list3 = new ArrayList[10];
        // 编译通过
        List<?>[] list4 = new ArrayList<?>[10];
        List[] list5 = new ArrayList[10];
        List<?> objects = list4[0];
        Object o = objects.get(0);
        System.out.println("123");

        ArrayWithTypeToken<Integer> arrayToken = new ArrayDemo(). new ArrayWithTypeToken<Integer>(Integer.class, 100);
        Integer[] array = arrayToken.create();
    }
    class ArrayWithTypeToken<T> {
        private T[] array;

        public ArrayWithTypeToken(Class<T> type, int size) {
            array = (T[]) Array.newInstance(type, size);
        }

        public void put(int index, T item) {
            array[index] = item;
        }

        public T get(int index) {
            return array[index];
        }

        public T[] create() {
            return array;
        }
    }
}




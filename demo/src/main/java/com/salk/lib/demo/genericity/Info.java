package com.salk.lib.demo.genericity;

import java.util.Iterator;
import java.util.List;

/**
 * 泛型的上下线 <?> 无限制通配符 <? extends E> extends 关键字声明了类型的上界，表示参数化的类型可能是所指定的类型，或者是此类型的子类 <? super E> super
 * 关键字声明了类型的下界，表示参数化的类型可能是指定的类型，或者是此类型的父类
 * 1. 如果参数化类型表示一个 T 的生产者，使用 < ? extends T>; 多个限制使用& < ? extends T & M>
 * 2. 如果它表示一个T 的消费者，就使用 < ? super T>；
 * 3. 如果既是生产又是消费，那使用通配符就没什么意义了，因为你需要的是精确的参数类型
 * 
 * @author salkli
 * @since 2022/7/26
 **/
public class Info<T extends Number, V> {
    private T t;
    private V v;

    public Info(T t) {
        this.t = t;
    }

    public Info(T t, V v) {
        this.t = t;
        this.v = v;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }

    public static void execute(Info<Integer, ? super String> func) {
        Number t = func.getT();
        System.out.println(t);
    }

    public static void execute2(Info<Float, ? extends Number> func) {
        Number t = func.getT();
        System.out.println(t);
    }

    public static void execute3(Number f){
        System.out.println(f);
    }
    public static void execute4(Float f){
        System.out.println(f);
    }


    private <E extends Comparable<? super E>> E max(List<? extends E> e1) {
        if (e1 == null) {
            return null;
        }
        // 迭代器返回的元素属于 E 的某个子类型
        Iterator<? extends E> iterator = e1.iterator();
        E result = iterator.next();
        while (iterator.hasNext()) {
            E next = iterator.next();
            if (next.compareTo(result) > 0) {
                result = next;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // 编译不过
        // Info<String> stringInfo = new Info<String>("12");
        Info<Integer, ?> integerInfo = new Info<>(1);
        // 编译不通过
        // execute(new Info<Integer,Integer>(123,123));
        // 编译通过
        execute(new Info<Integer, Object>(1, 123));
        // 编译不通过
        // execute2(new Info<Float,String>(1,"2"));
        execute2(new Info<Float, Long>(1f, 2L));
        execute3(new Integer(1));
    }
}

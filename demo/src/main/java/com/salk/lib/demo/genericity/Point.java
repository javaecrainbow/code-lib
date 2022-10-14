package com.salk.lib.demo.genericity;

/**
 * 泛型类
 * 
 * @author salkli
 * @since 2022/7/26
 **/
public class Point<T> {
    private T type;

    public Point(T type) {
        this.type = type;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public static void main(String[] args) {

        Point<String> cycle = new Point<>("圆");
        System.out.println(cycle.getType());
        SalkMap<String, Integer> stringIntegerSalkMap = new SalkMap<>("1", 1);
        System.out.println(stringIntegerSalkMap.getK());
    }
}

/**
 * 多值泛型
 * 
 * @param <K>
 * @param <V>
 */
class SalkMap<K, V> {
    private K k;
    private V v;

    public SalkMap(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }
}

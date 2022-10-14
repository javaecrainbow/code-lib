package com.salk.lib.demo.genericity;

/**
 * @author salkli
 * @since 2022/7/26
 **/
public class Person {
    private Number age;

    public Person(Number age) {
        this.age = age;
    }

}
class Boy extends Person{

    public Boy(String age) {
    super(Integer.parseInt(age));
    }
}

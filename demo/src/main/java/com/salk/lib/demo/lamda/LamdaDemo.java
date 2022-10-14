package com.salk.lib.demo.lamda;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * @author salkli
 * @since 2022/7/22
 **/
public class LamdaDemo {

    public List<String> mock1() {
        List list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        return list;
    }

    public List<Integer> mock2() {
        List list = new ArrayList();
        list.add(1);
        list.add(-2);
        list.add(3);
        return list;
    }

    @Test
    public void filterTest() {
        List<String> strings = mock1();
        List<String> collect1 = strings.stream().filter(item -> true).collect(Collectors.toList());
        System.out.println("所有数据========");
        System.out.println(collect1);
        Stream<String> stringStream = strings.stream().filter((item) -> item.equals("1"));
        List<String> collect = strings.stream().filter(item -> item.equals("1")).collect(Collectors.toList());
        System.out.println(collect);
        List<String> collect2 = strings.stream().filter((item) -> item.equals("1")).collect(Collectors.toList());
        List<String> collect3 = strings.stream().filter((String s) -> s.equals("1")).collect(Collectors.toList());
        Predicate<String> predicate = item -> item.equals("1");
        Predicate<String> predicate2 = item -> item.startsWith("1");
        System.out.println("predicate and ========");
        strings.stream().filter(predicate.and(predicate2)).collect(Collectors.toList()).forEach(System.out::println);
    }

    @Test
    public void testMapReduce(){
        List<Integer> integers = Arrays.asList(100, 200, 300, 400);
        Integer integer = integers.stream().map(item -> item * 10).reduce((item, sum) -> item + sum).get();
        System.out.println(integer);
    }

    @Test
    public void testCollector(){
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 1, 2, 4, 3, 6, 7, 8);
        integers.stream().collect(Collectors.toList()).forEach(System.out::println);
        String joiner = integers.stream().map(item -> item.toString()).collect(Collectors.joining(","));
        System.out.println(joiner);
        integers.stream().collect(Collectors.toSet()).forEach(System.out::println);
        Map<Double, Integer> collect1 = integers.stream().collect(Collectors.toMap(item->Math.random(), Function.identity()));
        System.out.println(collect1);

    }

    @Test
    public void testFlatMap(){
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 1, 2, 4, 3, 6, 7, 8);
        List<Integer> integers2 = Arrays.asList(1, 2, 3, 4, 1, 2, 4, 3, 6, 7, 8);
        int size = integers.stream().flatMap(item -> integers2.stream()).collect(Collectors.toList()).size();
        System.out.println(size);
    }

    @Test
    public void testSelector() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 1, 2, 4, 3, 6, 7, 8);
        integers.stream().distinct().forEach(System.out::println);

    }
    @Test
    public void testMatch() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 1, 2, 4, 3, 6, 7, 8);
        integers.stream().anyMatch(item -> item.equals(1));
        integers.stream().noneMatch(item -> item.equals(1));
        integers.stream().allMatch(item -> item.equals(1));
    }


    @Test
    public void testStatistics(){
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 1, 2, 4, 3, 6, 7, 8);
        Integer integer = integers.stream().max(Comparator.comparing(item -> item)).get();
        System.out.println(integer);
        Optional<Integer> max = integers.stream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(max.get());
        IntSummaryStatistics intSummaryStatistics = integers.stream().mapToInt(item -> item).summaryStatistics();
        long sum = intSummaryStatistics.getSum();
        double average = intSummaryStatistics.getAverage();
        System.out.println(sum);
        System.out.println(average);
    }
    
    /**
     * 测试方法引用
     */
    @Test
    public void testMethodReference() {
        //构造引用 无参构造方法
        Supplier<Person> s = Person::new;
        List<Integer> strings = mock2();
        //对象::实例方法 Lambda表达式的(形参列表)与实例方法的(实参列表)类型，个数是对应
        strings.forEach(System.out::println);
        //类名::静态方法
        Stream<Double> generate = Stream.generate(Math::random);
        System.out.println(generate.findFirst().get());
        strings.stream().forEach(Math::abs);
        //类名::实例方法
        Set<String> sets=new TreeSet<>(String::compareTo);


    }

    @Test
    public void supplier() {

    }

    @Test
    public void accept() {

    }

    @Test
    public void function() {

    }

    @Test
    public void testPredicate() {

    }

    @Test
    public void forMatchReturn() {
        List<String> param = new ArrayList();
        param.add("1");
        param.add("2");
        param.add("3");
        param.stream().map(item -> item.toLowerCase()).collect(Collectors.toList());
    }


    @Test
    public void testPeek() {
        List<String> param = new ArrayList();
        param.add("1");
        param.add("2");
        param.add("3");
        List<String> collect = param.stream().peek(item -> {
            System.out.println(item);
        }).collect(Collectors.toList());
        System.out.println(collect);
    }

    static class Person {

        private String name;

        private Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}

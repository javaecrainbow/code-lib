package com.salk.lib.persist;

/**
 * 用户
 *
 * @author salkli
 * @date 2022/10/11
 * @since 2022/10/11
 */

public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public User setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }
}

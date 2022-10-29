package com.salk.lib.persist;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户
 *
 * @author salkli
 * @date 2022/10/11
 * @since 2022/10/11
 */

@TableName(value = "user")
public class UserPo {
    private Long id;
    private String name;
    @TableField(value = "age")
    private String age1;
    private String email;

    public UserPo(Long id, String name, String age1, String email) {
        this.id = id;
        this.name = name;
        this.age1 = age1;
        this.email = email;
    }

    public UserPo() {}

    public Long getId() {
        return id;
    }

    public UserPo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserPo setName(String name) {
        this.name = name;
        return this;
    }

    public String getAge1() {
        return age1;
    }

    public UserPo setAge1(String age1) {
        this.age1 = age1;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserPo setEmail(String email) {
        this.email = email;

        return this;
    }
}

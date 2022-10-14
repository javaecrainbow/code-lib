package com.salk.lib.persist;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.salk.lib.persist.mapper.UserMapper;


/**
 * 用户测试
 *
 * @author salkli
 * @date 2022/10/11
 */
@SpringBootTest
public class UserTest {

@Autowired
private  UserMapper userMapper;

    /**
     * 测试插入
     */
    @Test
    public void testInsert(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);

    }

}

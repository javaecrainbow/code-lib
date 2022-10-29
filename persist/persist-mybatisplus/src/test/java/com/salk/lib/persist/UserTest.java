package com.salk.lib.persist;

import java.util.List;

import com.salk.lib.persist.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



/**
 * 用户测试
 *
 * @author salkli
 * @date 2022/10/11
 */
@SpringBootTest
public class UserTest {

@Autowired
private UserMapper userMapper;

    /**
     * 测试查询
     */
    @Test
    public void testSelect(){
        List<UserPo> users = userMapper.selectList(null);
        System.out.println("姓名"+users.get(0).getName());

    }

    /**
     * 测试插入
     */
    @Test
    public void testInsert(){
        //List<UserPo> users = userMapper.selectList(null);
        //System.out.println("记录数1=========="+users.size());
        UserPo user = new UserPo(10L, "salk", "20", "salkking2006@gmail.com");
        int insert = userMapper.insert(user);
        List<UserPo> users2 = userMapper.selectList(null);
        System.out.println("记录数=========="+users2.size());
    }


}

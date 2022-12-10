package com.salk.lib.persist;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public void testSelectScript(){
        int i = userMapper.seletDeal();
        System.out.println("select from dual"+i);

    }

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
    public void testInsert()throws Exception{
        //List<UserPo> users = userMapper.selectList(null);
        //System.out.println("记录数1=========="+users.size());
        UserPo user = new UserPo(10L, "salk", "20", "salkking2006@gmail.com");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM-dd");
        String format = simpleDateFormat.format(new Date());
        Date parse = simpleDateFormat.parse(format);
        Time t=new Time(parse.getTime());
        System.out.println(t);
        user.setBirthday(t);
        int insert = userMapper.insert(user);
        List<UserPo> users2 = userMapper.selectList(null);
        Date birthday = users2.get(0).getBirthday();
        System.out.println(birthday);
        System.out.println("记录数=========="+users2.size());
    }


}

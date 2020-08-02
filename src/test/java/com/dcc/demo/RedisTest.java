package com.dcc.demo;

import com.alibaba.fastjson.JSON;
import com.dcc.demo.model.User;
import com.dcc.demo.redis.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest {
    @Resource
    private RedisUtil redisUtil;

    /**
     * 插入缓存数据
     */
    @Test
    public void set() {
        redisUtil.set("redis_key", "redis_vale");

        redisUtil.hPut("auth_user","1",new User("代长春",23).toString());
        redisUtil.hPut("auth_user","2",new User("陈思雨",23).toString());
    }

    /**
     * 插入缓存数据 list
     */
    @Test
    public void setList() {
        redisUtil.delete("auth_user_list");
        redisUtil.lRightPush("auth_user_list", JSON.toJSONString(new User("代长春",23)));
        redisUtil.lRightPush("auth_user_list",JSON.toJSONString(new User("陈思雨",23)));
        List<String> strList =  redisUtil.lRange("auth_user_list",0,-1);
        System.out.println(strList);
        List<User> userList = strList.stream().map((String s)-> JSON.parseObject(s,User.class)).collect(Collectors.toList());
        System.out.println(userList);

    }
    /**
     * 读取缓存数据
     */
    @Test
    public void get() {
        String value = redisUtil.get("redis_key");
        Map<Object, Object> stringmap = redisUtil.hGetAll("auth_user");
        System.out.println(value);
        System.out.println(stringmap);
        redisUtil.delete("role_all");
    }
}

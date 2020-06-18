package com.dcc.demo;

import com.dcc.demo.model.User;
import com.dcc.demo.redis.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Map;

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
     * 读取缓存数据
     */
    @Test
    public void get() {
        String value = redisUtil.get("redis_key");
        Map<Object, Object> stringmap = redisUtil.hGetAll("auth_user");
        System.out.println(value);
    }
}

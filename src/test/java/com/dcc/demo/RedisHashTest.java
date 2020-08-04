package com.dcc.demo;

import com.alibaba.fastjson.JSON;
import com.dcc.demo.common.CommonConstant;
import com.dcc.demo.model.Role;
import com.dcc.demo.redis.RedisUtil;
import com.dcc.demo.service.RoleService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisHashTest {
    private final Logger logger = LoggerFactory.getLogger(RedisHashTest.class);

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private RoleService roleService;
    /**
     * 插入缓存数据
     */
    @Test
    public void set() {

    }

    /**
     * 插入缓存数据 list
     */
    @Test
    public void setRoleList() {
        List<Role> roles = roleService.getAllRoles();
        Map<String,String> map = roles.stream().collect(Collectors.toMap((role)->JSON.toJSONString(role.getId()),(role1)->JSON.toJSONString(role1)));
        redisUtil.hPutAll(CommonConstant.ROLE_HASH_PRE,map);
        logger.info("roleList put:{}",map);
    }

    /**
     * 读取缓存数据
     */
    @Test
    public void getRoleList() {
        List<Role> roleList = new ArrayList<>();

        Map<Object,Object> map = redisUtil.hGetAll(CommonConstant.ROLE_HASH_PRE);
        map.keySet().stream().forEach(
         (Object s)->{
            String  object = (String)  map.get(s);
            Role role = (Role) JSON.parseObject(object,Role.class);
            roleList.add(role);
        });
        logger.info("roleList get:{}",roleList);
    }
}

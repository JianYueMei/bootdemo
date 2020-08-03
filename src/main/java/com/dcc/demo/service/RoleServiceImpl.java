package com.dcc.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dcc.demo.model.Role;
import com.dcc.demo.redis.RedisUtil;
import com.dcc.demo.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RoleServiceImpl implements RoleService{
    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    @Override
    public Role getRoleById(Long id) {
        Role role = null;
        if (redisUtil.hasKey("role_all")) {
            Object key =redisUtil.hGet("role_all",JSON.toJSONString(id));
            role =  (Role) JSON.parseObject((String)key,Role.class);
        }

        return role;
    }
    @Override
    public List<Role> getAllRolesFromHash() {
        if (redisUtil.hasKey("search_list")) {
            logger.info("===存在list");
        } else {
            redisUtil.set("search_list","search_list_ing");
            redisUtil.expire("search_list",10, TimeUnit.MINUTES);
        }
        if(redisUtil.hasKey("role_all")){
            Map<Object,Object> map = redisUtil.hGetAll("role_all");
            List<Role> roleList = new ArrayList<>();
            map.keySet().stream().forEach((Object s)->{
                String  key = (String)  map.get(s);
                Role role = (Role) JSON.parseObject(key,Role.class);
                roleList.add(role);
            });
//            List<Role> roleList1 = JSONArray.parseArray(map.entrySet().toString(),Role.class);
            return roleList;
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Role addRole(Role role) {
        role = roleRepository.save(role);
        if(role!=null && role.getId() !=null) {
            syncRole(role);
        }
//        int i=1/0;
        return role;
    }

    private void syncRole(Role role){
        redisUtil.hPut("role_all",JSON.toJSONString(role.getId()),JSON.toJSONString(role));
    }

    public Integer syncAllRoles(){
        Integer resu = 0;
        try {
            if(redisUtil.hasKey("role_all")){
                redisUtil.delete("role_all");
            }
            List<Role> roleList = roleRepository.findAll();
            for(Role r:roleList){
                syncRole(r);
            }
            redisUtil.expire("role_all",600,TimeUnit.SECONDS);
            resu = 1;
        } catch (Exception e){
            logger.error("同步用户报错:{}",e);
            return resu;
        }
        return resu;
    }
}

package com.dcc.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dcc.demo.model.Role;
import com.dcc.demo.redis.RedisUtil;
import com.dcc.demo.repository.RoleRepository;
import com.dcc.demo.vo.RoleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService{
    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RedisUtil redisUtil;

    private static final String ROLE_PRE =  "role-all";
    private static final String ROLE_KEY_PRE =  "role-id";

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public RoleVo getRoleById(Long id) {
        RoleVo role = null;
        if (redisUtil.hasKey(ROLE_PRE)) {
            Object value =redisUtil.hGet(ROLE_PRE, ROLE_KEY_PRE + JSON.toJSONString(id));
            role =  (RoleVo) JSON.parseObject((String)value,RoleVo.class);
        }
        return role;
    }

    @Override
    public List<RoleVo> getRoleByIds(List<Long> ids) {
        List<RoleVo> roleVos = new ArrayList<>();
        if(CollectionUtils.isEmpty(ids)){
            return null;
        }
        List<Object> idss = ids.stream().map(id->ROLE_KEY_PRE + JSON.toJSONString(id)).collect(Collectors.toList());
        if (redisUtil.hasKey(ROLE_PRE)) {
            List<Object> objects =redisUtil.hMultiGet(ROLE_PRE,idss);
            objects.stream().forEach((Object object) -> {
                RoleVo roleVo = JSON.parseObject((String)object,RoleVo.class);
                roleVos.add(roleVo);
            });
        }
        return roleVos;
    }

    @Override
    public List<Role> getAllRolesFromHash() {
        if (redisUtil.hasKey("search_list")) {
            logger.info("===存在list");
        } else {
            redisUtil.set("search_list","search_list_ing");
            redisUtil.expire("search_list",10, TimeUnit.MINUTES);
        }
        if(redisUtil.hasKey(ROLE_PRE)){
            Map<Object,Object> map = redisUtil.hGetAll(ROLE_PRE);
            List<Role> roleList = new ArrayList<>();
            map.keySet().stream().forEach((Object key)->{
                String  value = (String)  map.get(key);
                Role role = (Role) JSON.parseObject(value,Role.class);
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


    public Integer syncAllRoles(){
        Integer resu = 0;
        try {
            if(redisUtil.hasKey(ROLE_PRE)){
                redisUtil.delete(ROLE_PRE);
            }
            List<Role> roleList = roleRepository.findAll();
            Map<String, String> maps = roleList.stream().collect(
                        Collectors.toMap(
                            role->ROLE_KEY_PRE + JSON.toJSONString(role.getId()),
                            s1->JSON.toJSONString(s1))
                    );
            redisUtil.hPutAll(ROLE_PRE,maps);
//            for(Role r:roleList){
//                syncRole(r);
//            }
            redisUtil.expire(ROLE_PRE,600,TimeUnit.SECONDS);
            resu = 1;
        } catch (Exception e){
            logger.error("同步用户报错:{}",e);
            return resu;
        }
        return resu;
    }


    private void syncRole(Role role) {
        redisUtil.hPut(ROLE_PRE,ROLE_KEY_PRE + JSON.toJSONString(role.getId()),JSON.toJSONString(role));
    }



}

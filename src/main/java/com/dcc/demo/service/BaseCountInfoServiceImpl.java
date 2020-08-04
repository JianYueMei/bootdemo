package com.dcc.demo.service;

import com.alibaba.fastjson.JSON;
import com.dcc.demo.common.CommonConstant;
import com.dcc.demo.model.Role;
import com.dcc.demo.model.User;
import com.dcc.demo.redis.RedisUtil;
import com.dcc.demo.vo.BaseCountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
public class BaseCountInfoServiceImpl implements BaseCountInfoService {

    private final Logger logger = LoggerFactory.getLogger(BaseCountInfoServiceImpl.class);

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;


    private static final ExecutorService executorService = new ThreadPoolExecutor(1, 1,
            200L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadFactory() {
                final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = defaultFactory.newThread(r);
                    thread.setName("BaseCountInfoService-" + thread.getName());
                    return thread;
                }
            },
            new ThreadPoolExecutor.AbortPolicy());

    @Override
    public Integer syncBaseCountInfo() {
        logger.info("===syncBaseCountInfo====begin");

        Integer result = CommonConstant.RESULT_ERROR;

            if (redisUtil.hasKey(CommonConstant.BASE_COUNT_INFO)) {
                result = CommonConstant.RESULT_EXSIT;
                logger.info("===存在list return {}",result);
                return result;
            }

            BaseCountInfo baseCountInfo = new BaseCountInfo();
            baseCountInfo.setBaseName("baseName");
            redisUtil.hPut(CommonConstant.BASE_COUNT_INFO, JSON.toJSONString(baseCountInfo.getBaseName()),JSON.toJSONString(baseCountInfo));

            executorService.submit(()->{
                try {
                    List<User> users = userService.getAllUsers();
                    baseCountInfo.setUserCount(users!=null ? users.size() : -1);
                    baseCountInfo.setUserList(users);
                    List<Role> roles = roleService.getAllRoles();
                    baseCountInfo.setRoleCount(roles!=null ? roles.size() : -1);
                    baseCountInfo.setRoleList(roles);
                    redisUtil.hPut(CommonConstant.BASE_COUNT_INFO, JSON.toJSONString(baseCountInfo.getBaseName()),JSON.toJSONString(baseCountInfo));
                } catch (Exception e) {
                    logger.error("===线程池同步缓存数据====error:{}",e);
                } finally {
                    if (redisUtil.hasKey(CommonConstant.BASE_COUNT_INFO)) { //设置过期时间 防止一直存在
                        redisUtil.expire(CommonConstant.BASE_COUNT_INFO,10, TimeUnit.MINUTES);
                    }
                }
            });
            result = CommonConstant.RESULT_SUCCESS;
        logger.info("===syncBaseCountInfo====end  result:{}",result);
        return result;
    }

    @Override
    public BaseCountInfo getBaseCountInfo() {
        if (redisUtil.hasKey(CommonConstant.BASE_COUNT_INFO)) {
            String object = (String) redisUtil.hGet(CommonConstant.BASE_COUNT_INFO,JSON.toJSONString("baseName"));
            BaseCountInfo baseCountInfo = (BaseCountInfo) JSON.parseObject(object,BaseCountInfo.class);
            return  baseCountInfo;
        }
        return null;
    }
}

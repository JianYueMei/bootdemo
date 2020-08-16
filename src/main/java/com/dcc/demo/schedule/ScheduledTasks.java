package com.dcc.demo.schedule;

import com.dcc.demo.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private RoleService roleService;

    @Scheduled(cron = "0 0/1 * * * ?") // 在每5分钟触发
    public void syncAllRoles() {
        roleService.syncAllRoles();
        logger.info("我执行了");
    }
}

package com.dcc.demo.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

//@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@Configuration
public class JpaConfig {
    // 其中 dataSource 框架会自动为我们注入
//    @Autowired
//    private DataSource dataSource;

//    @Bean
//    public PlatformTransactionManager  transactionManager() {
//        new JpaTransactionManager(dataSource);
//        JpaTransactionManager jpaTransactionManager = (JpaTransactionManager) transactionManager;
//        jpaTransactionManager.setNestedTransactionAllowed(true);
//        return jpaTransactionManager;
//    }
//    @Bean
//    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
//        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
//        jpaTransactionManager.setNestedTransactionAllowed(true);
//        return jpaTransactionManager;
//    }
}

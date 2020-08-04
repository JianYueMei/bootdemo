package com.dcc.demo.service;


import com.dcc.demo.model.Role;
import com.dcc.demo.model.User;
import com.dcc.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Queue;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Queue userQueue;
    @Autowired
    private RoleService roleService;


    @Override
    public List<User> getAllUsers() {
        try {
            Thread.currentThread().sleep(60000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userRepository.findAll();
    }

    /**
     *      ServiceA {
     *          void methodA() {
     *              ServiceB.methodB();
     *          }
     *      }
     * -------------事务类型 -------------------
     * $$$ PROPAGATION_REQUIRED  methodA 如果methodA已经起了事务
     * $$$--1 PROPAGATION_REQUIRED  methodB 会共用同一个事务 如果出现异常，methodA和methodB作为一个整体都将一起回滚
     *
     * $$$--2 PROPAGATION_REQUIRES_NEW ServiceB.methodB   methodA的事务挂起  methodB会起一个新的事务
     *      1、如果ServiceB.methodB已经提交，那么ServiceA.methodA失败回滚，ServiceB.methodB是不会回滚的。
     *      2、如果ServiceB.methodB失败回滚，如果他抛出的异常被ServiceA.methodA的try..catch捕获并处理，ServiceA.methodA事务仍然可能提交；
     *      如果他抛出的异常未被ServiceA.methodA捕获处理，ServiceA.methodA事务将回滚。
     *      by dcc重要 --- methodA --noRollbackFor指定methodB抛出的异常  或者 try catch methodB 抛出的异常  那么methodA的事务不会回滚
     *                     methodA失败 此时 methodB已提交 methodB不会回滚
     *
     *
     * $$$--3 PROPAGATION_NESTED ServiceB.methodB  .methodA所在的事务就会挂起，ServiceB.methodB会起一个新的子事务并设置savepoint
     *      1、如果ServiceB.methodB已经提交，那么ServiceA.methodA失败回滚，ServiceB.methodB也将回滚。
     *      2、如果ServiceB.methodB失败回滚，如果他抛出的异常被ServiceA.methodA的try..catch捕获并处理，ServiceA.methodA事务仍然可能提交；
     *      如果他抛出的异常未被ServiceA.methodA捕获处理，ServiceA.methodA事务将回滚。
     *      by dcc重要 ---   org.springframework.transaction.NestedTransactionNotSupportedException: JpaDialect does not support savepoints
     *                      JPA hibernate不支持嵌套事务  Nested级别只对DataSourceTransactionManager事务管理器生效。
     * @param user
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User addUser(User user){
        User user1 = userRepository.save(user);
//        try {
//            roleService.addRole(new Role("role1","法务"));
//        } catch(ArithmeticException e){
//            logger.info("roleService报错");
//        }
//        int i=1/0;
//        jmsMessagingTemplate.convertAndSend(userQueue,user1);
        return user1;
    }
}

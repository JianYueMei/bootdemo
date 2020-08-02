package com.dcc.demo.util.annotation;

import org.aspectj.apache.bcel.classfile.Signature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

@Aspect
@Component
public class MyUserAspect {
    private final Logger logger = LoggerFactory.getLogger(MyUserAspect.class);

    @PostConstruct
    public void init(){
        logger.info("===========================================初始化aspect:{}","MyUserAspect.init");
    }

    @Pointcut("execution(public * com.dcc.demo.controller..User*.get*(..))*")
    public void pointcut(){

    }

    @Around("pointcut()")
    public Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature)proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        MyUserAnnotation myUserAnnotation = method.getDeclaredAnnotation(MyUserAnnotation.class);
        if(myUserAnnotation !=null ){
            logger.info("===========================================注解被识别:{}--{}","myUserAnnotation.process",myUserAnnotation.value());
        }

        return proceedingJoinPoint.proceed();
    }

    /**
     * 后置最终通知
     * @throws Throwable
     */
    @After("pointcut()")
    public void doAfter() throws Throwable {
        logger.info("=========================================== End ===========================================");
    }



}

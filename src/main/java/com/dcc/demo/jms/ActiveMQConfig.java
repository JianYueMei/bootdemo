package com.dcc.demo.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.converter.SimpleMessageConverter;


import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableJms
public class ActiveMQConfig {
    /**
     * 消息重试配置项
     * @return
     */
    @Bean
    public RedeliveryPolicy redeliveryPolicy(){
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setUseExponentialBackOff(true);//是否在每次失败重发是，增长等待时间
        redeliveryPolicy.setMaximumRedeliveryDelay(-1);//设置重发最大拖延时间，-1 表示没有拖延，只有setUseExponentialBackOff(true)时生效
        redeliveryPolicy.setMaximumRedeliveries(3);//重发次数
        redeliveryPolicy.setInitialRedeliveryDelay(1);//重发时间间隔
        redeliveryPolicy.setBackOffMultiplier(2);//第一次失败后重发前等待500毫秒，第二次500*2，依次递增
        redeliveryPolicy.setUseCollisionAvoidance(false);//是否避免消息碰撞
        return redeliveryPolicy;
    }

    @Bean
    public ActiveMQConnectionFactory factory(@Value("${spring.activemq.broker-url}")String url, RedeliveryPolicy redeliveryPolicy){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", url);
        factory.setRedeliveryPolicy(redeliveryPolicy);
        // 设置信任序列化包集合
        List<String> models = new ArrayList<>();
        models.add("java.lang");
        models.add("java.util");
        models.add("com.dcc.demo");
        factory.setTrustedPackages(models);
//        factory.setTrustAllPackages(true); //信任所有包
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory factory){
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setDeliveryMode(2);//设置持久化，1 非持久， 2 持久化
        jmsTemplate.setConnectionFactory(factory);
        /**
         SESSION_TRANSACTED = 0  事物提交并确认
         AUTO_ACKNOWLEDGE = 1    自动确认
         CLIENT_ACKNOWLEDGE = 2    客户端手动确认
         DUPS_OK_ACKNOWLEDGE = 3    自动批量确认
         INDIVIDUAL_ACKNOWLEDGE = 4    单条消息确认 activemq 独有
         */
        jmsTemplate.setSessionAcknowledgeMode(4);//消息确认模式
        return jmsTemplate;
    }

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate(ActiveMQConnectionFactory factory,JmsTemplate jmsTemplate) {
        JmsMessagingTemplate jmsMessagingTemplate = new JmsMessagingTemplate(factory);
        jmsMessagingTemplate.setMessageConverter(new SimpleMessageConverter());
        jmsMessagingTemplate.setJmsTemplate(jmsTemplate);
        return jmsMessagingTemplate;
    }

    @Bean("jmsListener")
    public DefaultJmsListenerContainerFactory listener(ActiveMQConnectionFactory factory){
        DefaultJmsListenerContainerFactory listener = new DefaultJmsListenerContainerFactory();
        listener.setConnectionFactory(factory);
        listener.setConcurrency("1-10");//设置连接数
        listener.setRecoveryInterval(1000L);//重连间隔时间
        listener.setSessionAcknowledgeMode(4);
        return listener;
    }

//    //springboot默认只配置queue类型消息，如果要使用topic类型的消息，则需要配置该bean
    @Bean("jmsTopicListenerContainerFactory")
    public JmsListenerContainerFactory jmsTopicListenerContainerFactory(ActiveMQConnectionFactory factory){
        DefaultJmsListenerContainerFactory jmsTopicListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        jmsTopicListenerContainerFactory.setConnectionFactory(factory);
        //这里必须设置为true，false则表示是queue类型
        jmsTopicListenerContainerFactory.setPubSubDomain(true);
        return jmsTopicListenerContainerFactory;
    }

    @Bean("userQueue")
    public Queue userQueue() {
        return new ActiveMQQueue("dcc.user.queue") ;
    }

    @Bean("dccQueue")
    public Queue dccQueue() {
        return new ActiveMQQueue("dcc.hello.queue") ;
    }


    @Bean("dccTopic")
    public Topic dccTopic() {
        return new ActiveMQTopic("dcc.hello.topic") ;
    }
}
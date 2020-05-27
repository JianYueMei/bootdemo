package com.dcc.demo.jms;

import com.dcc.demo.model.User;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Consumer {

    private volatile Map  map = new ConcurrentHashMap();
    //接收queue类型消息
    //destination对应配置类中ActiveMQQueue("dcc.hello.queue")设置的名字
    @JmsListener(destination="dcc.hello.queue")
    public void ListenHelloQueue(TextMessage message, Session session) throws JMSException {
        String msgid = message.getJMSMessageID();
        String msg = message.getText();
        System.out.println("监听器收到msg:" + msg + "消息id" + msgid);
        // 检测是否存在该消息id的key 如果不存在表示 第一次消费
        if (!map.containsKey(msgid)) {//// 如果存在 表示已经消费成功，在次接受该消息的时候，则手动签收 避免在次重试
            map.put(msgid,msg);
            int i =1/0;
        } else {
            // 如果存在 表示已经消费成功，在次接受该消息的时候，则手动签收 避免在次重试
            message.acknowledge();
        }
        System.out.println("Hello队列接收到queue消息：" + msg);
    }

    @JmsListener(destination="dcc.user.queue")
    public void ListenUserQueue(User user){
        System.out.println("User队列接收到queue消息：" + user.toString());
    }

    //接收topic类型消息
    //destination对应配置类中ActiveMQTopic("springboot.topic")设置的名字
    //containerFactory对应配置类中注册JmsListenerContainerFactory的bean名称
    @JmsListener(destination="dcc.hello.topic", containerFactory = "jmsTopicListenerContainerFactory")
    public void ListenTopic(String msg){
        System.out.println("Hello topic接收到topic消息：" + msg);
    }
}

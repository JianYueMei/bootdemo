package com.dcc.demo.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

@RestController
public class Producer {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue dccQueue;

    @Autowired
    private Topic dccTopic;

    //发送queue类型消息
    @GetMapping("/helloQueue")
    public void sendQueueMsg(String msg){
        jmsMessagingTemplate.convertAndSend(dccQueue, msg);
    }

    //发送topic类型消息
    @GetMapping("/topic")
    public void sendTopicMsg(String msg){
        jmsMessagingTemplate.convertAndSend(dccTopic, msg);
    }

}


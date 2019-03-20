package com.conhj.server.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.conhj.server.config.TopicExchangeConfig;

@Service
@RabbitListener(queues = TopicExchangeConfig.order)
public class NotifyOrder {
    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver order  : " + hello);
    }

}

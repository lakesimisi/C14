package com.conhj.server.config;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQImpl;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicExchangeConfig {

    public final static String login = "topic.login";
    public final static String order = "topic.order";
    public final static String event = "topic.event";

    // 消息队列的声明
    @Bean
    public Queue queueLogin() {
        return new Queue(TopicExchangeConfig.login);
    }

    @Bean
    public Queue queueOrder() {
        return new Queue(TopicExchangeConfig.order);
    }
    @Bean
    public Queue queueEvent() {
        return new Queue(TopicExchangeConfig.event);
    }

    // 交换机的声明
    @Bean
    TopicExchange exchange() {
        return new TopicExchange("exchange");
    }

    // 消息队列的绑定
    @Bean
    Binding bindingExchangeLogin(Queue queueLogin, TopicExchange exchange) {
        return BindingBuilder.bind(queueLogin).to(exchange).with("topic.login");//接收登录消息
    }

    @Bean
    Binding bindingExchangeOrder(Queue queueOrder, TopicExchange exchange) {
        return BindingBuilder.bind(queueOrder).to(exchange).with("topic.order");//接收订单消息
    }
    @Bean
    Binding bindingExchangeEvent(Queue queueOrder, TopicExchange exchange) {
        return BindingBuilder.bind(queueOrder).to(exchange).with("topic.#");//接收所有消息
    }
}

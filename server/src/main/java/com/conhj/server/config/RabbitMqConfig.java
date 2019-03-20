package com.conhj.server.config;


import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RabbitMqConfig {

    /*此处静态常量，在原作中为了保证名称统一使用，可以不声明*/
    public static final String FOO_EXCHANGE = "callback.exchange.foo";
    public static final String FOO_EXCHANGE_TOPIC = "callback.topic.exchange.foo";
    public static final String FOO_ROUTINGKEY = "callback.routingkey.foo";
    public static final String FOO_QUEUE = "callback.queue.foo";
    /*end*/

    /*显式使用配置文件信息*/
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;
    @Value("${spring.rabbitmq.publisher-confirms}")
    private boolean publisherConfirms;
    /*end*/

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        /** 如果要进行消息回调，则这里必须要设置为true */
        connectionFactory.setPublisherConfirms(publisherConfirms);
        return connectionFactory;
    }

    @Bean
    /** 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调  类为最后一次设置 */
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    /*
     @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)这个是说在每次注入的时候回自动创建一个新的bean实例
     @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)单例模式，在整个应用中只能创建一个实例
     @Scope(value=WebApplicationContext.SCOPE_GLOBAL_SESSION)全局session中的一般不常用
     @Scope(value=WebApplicationContext.SCOPE_APPLICATION)在一个web应用中只创建一个实例
     @Scope(value=WebApplicationContext.SCOPE_REQUEST)在一个请求中创建一个实例
     @Scope(value=WebApplicationContext.SCOPE_SESSION)每次创建一个会话中创建一个实例
     */
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());  //如果上面未使用显示调用声明 ConnectionFactory,此处运行会报错:ConnectionFactory is null
        return template;
    }
}
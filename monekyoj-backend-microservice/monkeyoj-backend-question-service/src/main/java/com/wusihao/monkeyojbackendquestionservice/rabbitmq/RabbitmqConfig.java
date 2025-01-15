package com.wusihao.monkeyojbackendquestionservice.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wusihao
 * @date: 2023/9/11 16:50
 * @version: 1.0
 * @description:
 */
@Configuration
public class RabbitmqConfig {
    // 执行代码交换机配置
    @Bean
    public DirectExchange executeCodeExchange() {
        return ExchangeBuilder.directExchange(RabbitmqExchangeName.executeCodeExchange).build();
    }

    // 执行Java 原生代码队列配置
    @Bean
    public Queue javaNativeCodeExecuteQueue() {
        return QueueBuilder.durable(RabbitmqQueueName.javaNativeCodeExecuteQueue).build();
    }

    // 执行代码交换级绑定 Java 原生代码执行队列
    @Bean
    public Binding executeCodeExchangeBindJavaNativeCodeExecuteQueue(DirectExchange executeCodeExchange, Queue javaNativeCodeExecuteQueue) {
        return BindingBuilder.bind(javaNativeCodeExecuteQueue).to(executeCodeExchange).with(RabbitmqRoutingName.javaNativeCodeExecuteRouting);
    }
}

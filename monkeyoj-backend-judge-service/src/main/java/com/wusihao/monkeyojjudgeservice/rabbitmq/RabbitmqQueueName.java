package com.wusihao.monkeyojjudgeservice.rabbitmq;

/**
 * @author: wusihao
 * @date: 2023/9/11 16:41
 * @version: 1.0
 * @description:
 */
public class RabbitmqQueueName {
    // Java原生代码执行队列
    public static final String javaNativeCodeExecuteQueue = "javaNativeCodeExecuteQueue";
    // Java远生代码执行死信队列
    public static final String javaNativeCodeExecuteDlxQueue = "javaNativeCodeExecuteQueue";
}

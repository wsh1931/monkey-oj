package com.wusihao.monkeyojjudgeservice.rabbitmq;

/**
 * @author: wusihao
 * @date: 2023/9/11 16:41
 * @version: 1.0
 * @description:
 */
public class RabbitmqRoutingName {
    // Java原生代码执行路由
    public static final String javaNativeCodeExecuteRouting = "javaNativeCodeExecuteRouting";

    // Java远生代码执行死信路由
    public static final String javaNativeCodeExecuteDlxRouting = "javaNativeCodeExecuteDlxRouting";
}

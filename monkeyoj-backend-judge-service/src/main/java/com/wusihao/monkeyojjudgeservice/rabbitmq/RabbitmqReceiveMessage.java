package com.wusihao.monkeyojjudgeservice.rabbitmq;


import cn.hutool.json.JSONObject;
import com.rabbitmq.client.Channel;
import com.wusihao.monkeyojjudgeservice.judge.JudgeService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Slf4j
public class RabbitmqReceiveMessage {
    @Resource
    private JudgeService judgeService;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {RabbitmqQueueName.javaNativeCodeExecuteQueue}, ackMode = "MANUAL")
    public void receiveJavaNativeCodeExecute(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        System.err.println("deliveryTag = " + deliveryTag);
        JSONObject data = new JSONObject(message);
        String event = data.getStr("event");
        try {
            if (EventConstant.javaNativeCodeExecute.equals(event)) {
                log.info("触发receiveJavaNativeCodeExecute 事件为 = {}", event);
                Long questionSubmitId = data.getLong("questionSubmitId");
                System.err.println("questionSubmitId: " + questionSubmitId);
                judgeService.doJudge(questionSubmitId);
            }

            // 默认为取走后自动确认
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.info("触发receiveJavaNativeCodeExecute 异常事件为 = {}: 异常问题为: {}", event, e);
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
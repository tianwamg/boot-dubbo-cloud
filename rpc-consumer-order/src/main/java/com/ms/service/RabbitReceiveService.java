package com.ms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ通用消息接收服务
 */
@Service
public class RabbitReceiveService {

    private static final Logger log = LoggerFactory.getLogger(RabbitReceiveService.class);

    @Autowired
    private Environment env;

    @RabbitListener(queues = {"${mq.kill.success.email.queue}"},containerFactory = "singleListenerContainer")
    public void consumeEmailMsg(String orderNo){
        try{
            log.info("秒杀异步邮件通知-接收消息:{}",orderNo);
        }catch (Exception e){
            log.error("秒杀异步邮件通知-接收消息-发生异常：",e.fillInStackTrace());
        }
    }
}

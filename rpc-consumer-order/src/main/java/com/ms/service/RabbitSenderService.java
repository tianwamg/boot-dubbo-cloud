package com.ms.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ通用发送消息
 */
@Service
public class RabbitSenderService {

    public static final Logger log = LoggerFactory.getLogger(RabbitSenderService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    //秒杀成功发送异步邮件通知消息
    public void sendKillSuccessEmailMsg(String orderNo){
        log.info("秒杀成功异步发送邮件通知消息-准备发送消息：{}",orderNo);
        try{
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.setExchange(env.getProperty("mq.kill.success.email.exchange"));
            rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.success.email.routing.key"));

            rabbitTemplate.convertAndSend((Object) orderNo, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    MessageProperties messageProperties  = message.getMessageProperties();
                    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,String.class);
                    return message;
                }
            });
        }catch (Exception e){
            log.error("秒杀成功异步发送邮件通知消息-发生异常，消息为：{}",orderNo,e.fillInStackTrace());
        }
    }

    //秒杀成功后生成抢购订单-发送信息进入死信队列，超时订单失效
    public void sendKilSuccessOrderExpireMsg(final String orderNo){
        log.info("秒杀成功后生成抢购订单-发送信息入死信队列-准备发送消息：{}",orderNo);
        try{
            if(StringUtils.isNotBlank(orderNo)){
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(env.getProperty("mq.kill.success.real.exchange"));
                rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.success.real.routing.key"));
                rabbitTemplate.convertAndSend((Object) orderNo, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        MessageProperties properties = message.getMessageProperties();
                        properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        properties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,String.class);
                        properties.setExpiration(env.getProperty("mq.kill.success.expire"));
                        return message;
                    }
                });
            }
        }catch (Exception e){
            log.error("秒杀成功后生成抢购订单-发送信息入死信队列，等待着一定时间失效超时未支付的订单-发生异常，消息为：{}",orderNo,e.fillInStackTrace());
        }
    }
}

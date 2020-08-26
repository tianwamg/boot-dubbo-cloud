package com.ms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RabbitmqConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(RabbitmqConfiguration.class);

    @Autowired
    private Environment environment;

    @Autowired
    private CachingConnectionFactory connectionFactory;

    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    //单一消费者
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory singleListenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        //factory.setTxSize();
        return factory;
    }

    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory,connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //确认消费模式
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setConcurrentConsumers(environment.getProperty("spring.rabbitmq.listener.simple.concurrency",int.class));
        factory.setMaxConcurrentConsumers(environment.getProperty("spring.rabbitmq.listener.simple.max-concurrency",int.class));
        factory.setPrefetchCount(environment.getProperty("spring.rabbitmq.listener.simple.prefetch",int.class));
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMandatory(true);
        template.setConfirmCallback((correlationData, b, s) -> {
            logger.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, b, s);
        });
        template.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                logger.warn("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message);

            }
        });
        return template;
    }

    @Bean
    public Queue successEmailQueue(){
        return new Queue(environment.getProperty("mq.kill.success.email.queue"),true);
    }

    @Bean
    public TopicExchange successEmailExchage(){
        return new TopicExchange(environment.getProperty("mq.kill.success.email.exchange"),true,false);
    }

    @Bean
    public Binding successEmailBinding(){
        return BindingBuilder.bind(successEmailQueue()).to(successEmailExchage()).with(environment.getProperty("mq.kill.success.email.routing.key"));
    }
}

package com.ms;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@EnableDubbo
@SpringBootApplication
public class RpcConsumerOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcConsumerOrderApplication.class, args);
    }

}

package com.ms;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableDubbo
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class RpcConsumerUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcConsumerUserApplication.class, args);
    }

}

package com.ms;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.ms.mapper")
@EnableDiscoveryClient
@EnableDubbo
@SpringBootApplication
public class RpcProviderUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcProviderUserApplication.class, args);
    }

}

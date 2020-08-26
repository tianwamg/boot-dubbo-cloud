package com.ms.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "rpc-consumer-order")
public interface FeignService {

    @GetMapping("/order/h")
    public String hello();
}

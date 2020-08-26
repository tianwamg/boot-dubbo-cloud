package com.ms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class helloControlle {

    @GetMapping("/h")
    public String hello(){
        return "hello order";
    }
}

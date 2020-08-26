package com.ms.controller;

import com.ms.api.facade.user.IUserService;
import com.ms.api.model.user.SysUser;
import com.ms.feign.FeignService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @DubboReference
    IUserService iUserService;

    @Autowired
    FeignService feignService;

    @GetMapping("/hello")
    public SysUser hello(){
        return iUserService.selectyByPrimaryKey(1);
    }

    @GetMapping("/h")
    public String h(){
        return feignService.hello();
    }
}

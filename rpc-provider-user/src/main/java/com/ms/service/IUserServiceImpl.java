package com.ms.service;

import com.ms.api.facade.user.IUserService;
import com.ms.api.model.user.SysUser;
import com.ms.mapper.ISysUserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@DubboService
public class IUserServiceImpl implements IUserService {

    @Autowired
    ISysUserMapper iSysUserMapper;

    public SysUser selectyByPrimaryKey(Integer id){
        /*try {
            int i=0;
            TimeUnit.SECONDS.sleep(6);
            System.out.println(++i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return iSysUserMapper.selectByPrimaryKey(id);
    }
}

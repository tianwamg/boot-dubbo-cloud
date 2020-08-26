package com.ms;

import com.ms.api.model.user.SysUser;
import com.ms.mapper.ISysUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RpcProviderUserApplicationTests {

    @Autowired
    ISysUserMapper iSysUserMapper;

    @Test
    public void contextLoads() {
        SysUser sysUser = iSysUserMapper.selectByPrimaryKey(1);
        System.out.println(123);
    }

}

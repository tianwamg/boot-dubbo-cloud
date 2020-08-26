package com.ms.mapper;

import com.ms.api.model.user.SysUser;
import org.springframework.stereotype.Repository;

@Repository
public interface ISysUserMapper {

    SysUser selectByPrimaryKey(Integer id);
}

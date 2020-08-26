package com.ms.api.facade.user;

import com.ms.api.model.user.SysUser;

public interface IUserService {

    public SysUser selectyByPrimaryKey(Integer id);
}

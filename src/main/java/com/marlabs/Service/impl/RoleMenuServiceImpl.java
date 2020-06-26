package com.marlabs.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.marlabs.Mapper.BaseMapper;
import com.marlabs.Mapper.RoleMenuMapper;
import com.marlabs.Mapper.UserMapper;
import com.marlabs.Model.RoleMenu;
import com.marlabs.Service.IRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service(interfaceClass= IRoleMenuService.class, timeout=30000)
@Component
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenu> implements IRoleMenuService{
    @Autowired
    private RoleMenuMapper mapper;
    @Override
    public BaseMapper<RoleMenu> getMapper() {
        return mapper;
    }
}

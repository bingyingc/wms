package com.marlabs.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.marlabs.Mapper.BaseMapper;
import com.marlabs.Model.RoleMenu;
import com.marlabs.Model.RolePermission;
import com.amblelogistic.Service.IRoleMenuService;
import com.amblelogistic.Service.IRolePermissionService;
import org.springframework.stereotype.Component;

@Service(interfaceClass= IRolePermissionService.class, timeout=30000)
@Component
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermission> implements IRolePermissionService{
    @Override
    public BaseMapper<RolePermission> getMapper() {
        return null;
    }
}

package com.marlabs.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.marlabs.Mapper.BaseMapper;
import com.marlabs.Mapper.PermissionMapper;
import com.marlabs.Model.Menu;
import com.marlabs.Model.Permission;
import com.marlabs.Service.IMenuService;
import com.marlabs.Service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Service(timeout=30000)
@Component
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements IPermissionService {
    @Autowired
    PermissionMapper mapper;

    @Override
    public BaseMapper<Permission> getMapper() {
        return mapper;
    }

    @Override
    public List<Permission> queryByRoleId(Long roleId) {
        return mapper.queryByRoleId(roleId);
    }
}

package com.marlabs.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.marlabs.Mapper.BaseMapper;
import com.marlabs.Mapper.MenuMapper;
import com.marlabs.Model.Menu;
import com.marlabs.Service.IMenuService;
import com.marlabs.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Service(interfaceClass= IMenuService.class, timeout=30000)
@Component
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements IMenuService {
    @Autowired
    MenuMapper mapper;

    @Override
    public BaseMapper<Menu> getMapper() {
        return mapper;
    }

    @Override
    public List<Menu> queryByRoleId(Long roleId) {
        return mapper.queryByRoleId(roleId);
    }
}

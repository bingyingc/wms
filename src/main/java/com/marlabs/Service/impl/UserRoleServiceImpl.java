package com.marlabs.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.marlabs.Mapper.UserRoleMapper;
import com.marlabs.Model.UserRole;
import com.marlabs.Service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass= IUserRoleService.class, timeout=30000)
@Component
@Transactional
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements IUserRoleService{

    @Autowired
    UserRoleMapper mapper;

    //change from BaseMapper<UserRole> to UserRoleMapper still have error."Invalid bound statement (not found)"
    @Override
    public UserRoleMapper getMapper() {
        return mapper;
    }

    @Override
    public List<UserRole> queryList(Map<String, Object> paramMap){
       return mapper.queryList(paramMap);
    }

    @Override
    public int insert(UserRole record) {
        return 0;
    }
}

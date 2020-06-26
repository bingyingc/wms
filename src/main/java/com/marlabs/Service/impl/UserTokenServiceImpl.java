package com.marlabs.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.marlabs.Mapper.UserTokenMapper;
import com.marlabs.Model.UserToken;
import com.marlabs.Service.IUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass= IUserTokenService.class, timeout=30000)
@Component
@Transactional
public class UserTokenServiceImpl extends BaseServiceImpl<UserToken> implements IUserTokenService{

    @Autowired
    private UserTokenMapper mapper;


    public UserTokenMapper getMapper() {
        return mapper;
    }

    @Override
    public int insert(UserToken record) {
        mapper.insert(record);
        return 0;
    }


}
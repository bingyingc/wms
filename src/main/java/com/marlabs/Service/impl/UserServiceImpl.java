package com.marlabs.Service.impl;

import com.marlabs.Mapper.UserMapper;
import com.marlabs.Model.User;
import com.marlabs.Service.IUserService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass=IUserService.class, timeout=30000)
@Component
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService{
    @Autowired
    private UserMapper mapper;


    public UserMapper getMapper() {
        return mapper;
    }

    @Override
    public int insert(User record) {
        mapper.insert(record);
        return record.getUserId().intValue();
    }

    @Override
    public List<User> queryUser(Map<String, Object> paramMap) {
        return mapper.queryUser(paramMap);
    }

    public Page<User> queryUserPage(Map<String, Object> param,
                                    Page<User> page) {
        param.put("page", page);
        mapper.queryList(param);
        return page;
    }

    @Override
    public List<User> queryList(Map<String, Object> paramMap){
        return mapper.queryList(paramMap);
    }
}

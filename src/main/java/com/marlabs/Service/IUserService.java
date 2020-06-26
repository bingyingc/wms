package com.marlabs.Service;

import com.marlabs.Model.User;

import java.util.List;
import java.util.Map;

public interface IUserService extends IBaseService<User> {

    List<User> queryUser(Map<String, Object> paramMap);

//    public Page<User> queryUserPage(Map<String, Object> param,Page<User> page);
}
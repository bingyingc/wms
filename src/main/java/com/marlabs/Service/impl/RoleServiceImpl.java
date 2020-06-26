package com.marlabs.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.marlabs.Mapper.RoleMapper;
import com.marlabs.Model.Role;
import com.marlabs.Service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Service(interfaceClass= IRoleService.class, timeout=30000)
@Component
public class RoleServiceImpl extends  BaseServiceImpl<Role> implements  IRoleService{

        @Autowired
        private RoleMapper mapper;


        public RoleMapper getMapper() {
            return mapper;
        }

        @Override
        public int insert(Role record) {
            mapper.insert(record);
            return record.getRoleId().intValue();
        }

        @Override
        public List<Role> queryList(Map<String, Object> paramMap){
            return mapper.queryList(paramMap);
        }

}

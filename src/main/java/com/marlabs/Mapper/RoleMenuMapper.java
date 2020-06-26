package com.marlabs.Mapper;

import com.marlabs.Model.RoleMenu;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMenuMapper extends BaseMapper<RoleMenu>{
    int deleteByPrimaryKey(RoleMenu key);

    int insert(RoleMenu record);

    int insertSelective(RoleMenu record);
}
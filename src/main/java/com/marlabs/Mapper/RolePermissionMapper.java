package com.marlabs.Mapper;


import com.marlabs.Model.RolePermission;

public interface RolePermissionMapper {
    int deleteByPrimaryKey(RolePermission key);

    int insert(RolePermission record);

    int insertSelective(RolePermission record);
}
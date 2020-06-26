package com.marlabs.Mapper;

import com.marlabs.Model.User;
import com.marlabs.Model.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole>{
    int deleteByPrimaryKey(UserRole key);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    public List<UserRole> queryList(Map<String, Object> param);
}
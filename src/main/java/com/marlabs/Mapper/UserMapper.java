package com.marlabs.Mapper;

import com.marlabs.Model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import com.ffcs.icity.common.mybatis.MyBatisRepository;
import org.springframework.stereotype.Repository;

//@MyBatisRepository
@Repository
public interface UserMapper extends BaseMapper<User> {

    public List<User> queryUser(Map<String, Object> paramMap);

    public List<User> queryList(Map<String, Object> param);

    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}
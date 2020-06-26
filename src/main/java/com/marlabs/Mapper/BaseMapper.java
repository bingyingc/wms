package com.marlabs.Mapper;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {

    public int deleteByPrimaryKey(Long id);

    public int insert(T record);

    public int insertSelective(T record);
    public T selectByPrimaryKey(Long id);

    //public T queryByNum(Long num);

    public int updateByPrimaryKeySelective(T record);

    public int updateByPrimaryKey(T record);

    public List<T> query(Map<String, Object> paramMap);

    public int queryByCount(Map<String, Object> paramMap);

    public int queryBySum(Map<String, Object> paramMap);
}

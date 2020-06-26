package com.marlabs.Service.impl;

import com.marlabs.Mapper.BaseMapper;
import com.marlabs.Service.IBaseService;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<T> implements IBaseService<T> {

    public abstract BaseMapper<T> getMapper();

    public int deleteByPrimaryKey(Long id) {
        return getMapper().deleteByPrimaryKey(id);
    }

    public int insert(T record) {
        return getMapper().insert(record);
    }

    public int insertSelective(T record) {
        return getMapper().insertSelective(record);
    }
    //@Cacheable(value = "common",key="'id_'+#id")
    public T selectByPrimaryKey(Long id) {
        return (T) getMapper().selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(T record) {
        return getMapper().updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(T record) {
        return getMapper().updateByPrimaryKey(record);
    }

    public List<T> queryList(Map<String, Object> paramMap) {
        return getMapper().query(paramMap);
    }

    public Page<T> queryPage(Map<String, Object> paramMap, Page<T> page) {
        paramMap.put("page", page);
        getMapper().query(paramMap);
        return page;
    }

    public int queryByCount(Map<String, Object> paramMap) {
        return getMapper().queryByCount(paramMap);
    }

    public int queryBySum(Map<String, Object> paramMap) {
        return getMapper().queryBySum(paramMap);
    }

}
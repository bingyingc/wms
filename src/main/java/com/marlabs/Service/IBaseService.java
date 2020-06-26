package com.marlabs.Service;

import java.util.List;
import java.util.Map;

//import com.ffcs.icity.common.mybatis.Page;

/**
 * 提供service基础接口
 *
 * @author shamee-loop
 * @param <T>
 */
public interface IBaseService<T> {

    public int deleteByPrimaryKey(Long id);

    public int insert(T record);

    public int insertSelective(T record);

    public T selectByPrimaryKey(Long id);

    //public T queryByNum(Long num);

    public int updateByPrimaryKeySelective(T record);

    public int updateByPrimaryKey(T record);

    public List<T> queryList(Map<String, Object> paramMap);

//    public Page<T> queryPage(Map<String, Object> paramMap, Page<T> page);

    public int queryByCount(Map<String, Object> paramMap);

    public int queryBySum(Map<String, Object> paramMap);

}


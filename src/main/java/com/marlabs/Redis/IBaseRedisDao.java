package com.marlabs.Redis;

import java.util.Map;

public interface IBaseRedisDao<T> {

    public  boolean add(final T record, Map<String, Object> param);
    public  boolean detele(String key);
    public boolean update(final T record, Map<String, Object> param);
    public T get(final String key);
}
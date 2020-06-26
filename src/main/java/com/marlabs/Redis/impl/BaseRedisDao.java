package com.marlabs.Redis.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;

public class BaseRedisDao<K,V> {
	      
	    @Resource
	    protected RedisTemplate<K, V> redisTemplate;  
	  
	    /** 
	     * 设置redisTemplate 
	     * @param redisTemplate the redisTemplate to set 
	     */  
	    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {  
	        this.redisTemplate = redisTemplate;  
	    }  
	      
	    /** 
	     * 获取 RedisSerializer 
	     * <br>------------------------------<br> 
	     */  
	    protected RedisSerializer<String> getRedisSerializer() {  
	        return redisTemplate.getStringSerializer();  
	    } 
}

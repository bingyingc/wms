package com.marlabs.Redis.impl;

import java.util.List;
import java.util.Map;

import com.marlabs.Model.User;
import com.marlabs.Redis.IUserDao;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

@Service(interfaceClass= IUserDao.class, timeout=30000)
@Component
public class UserDaoImpl extends BaseRedisDao<String, User> implements IUserDao{

	@Override
	public boolean add(final User user,final Map<String, Object> param) {
		boolean result = false;
		
			result = redisTemplate.execute(new RedisCallback<Boolean>() {  
	            public Boolean doInRedis(RedisConnection connection)  
	                    throws DataAccessException {  
	                RedisSerializer<String> serializer = getRedisSerializer();  
	                String token = (String) param.get("token");
	                byte[] key  = serializer.serialize(token);  
	                byte[] name = serializer.serialize(JSONObject.toJSONString(user)); 
	               
	                return connection.setNX(key, name);  
	            }  
	        });
			//redisTemplate.delete("ODBjYzA5YjMyZTJmNGQwOWJiNzFiMzdmZmJkM2JjMTNfMTUxMzIyNzU1NTUzMg==");
		 if(result){
			 //detele("userAsset_userId_"+userAssets.getUserId()+"_"+userAssets.getVersion());
		 }
		 System.out.println("result:"+result);
	        return result;
	
	}

	@Override
	public boolean detele(final String token) {
		boolean result = false;
		
		return result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            public Boolean doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                RedisSerializer<String> serializer = getRedisSerializer();  
               
                byte[] key  = serializer.serialize(token + "");  
                System.out.println("key:"+token);
                Long count = connection.del(key);
                System.err.println("----"+count);
				return true;
            }  
        });
}
	public void delete(List<String> keys) {  
        redisTemplate.delete(keys);  
    } 
	
	@Override
	public synchronized boolean update(final User user,final Map<String, Object> param) {
	try {
		if(param == null){
			return false;
		}
		String key = (String) param.get("token");  
        if (get(key) == null) {  
            throw new NullPointerException("数据行不存在, key = " + key);  
        }  
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            public Boolean doInRedis(RedisConnection connection)  
                    throws DataAccessException {
              
    		  RedisSerializer<String> serializer = getRedisSerializer();
    		 
              byte[] key  = serializer.serialize((String) param.get("token")+"");  
              byte[] name = serializer.serialize(JSONObject.toJSONString(user));  
     
           
             if( connection.setNX(key, name)){
            	  System.out.println("update redis result ==================true");
            	 
              }else{
            	  System.out.println("update redis result ==================fasle");
            	  
            	  return update(user, param);
              }
              
			return true;
            }  
        });
        return result;
	} catch (Exception e) {
		e.printStackTrace();
	}
        return true;
	}

	@Override
	public User get(final String keyId) {
		User result = redisTemplate.execute(new RedisCallback<User>() {  
            public User doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                RedisSerializer<String> serializer = getRedisSerializer();  
                byte[] key = serializer.serialize(keyId);  
                byte[] value = connection.get(key);  
                if (value == null) {  
                    return null;  
                }  
                String name = serializer.deserialize(value);  
                JSONObject json = JSONObject.parseObject(name);
                User assets =  JSONObject.toJavaObject(json, User.class);
                //System.out.println("assets----------"+assets.toString());
                return assets;  
            }  
        });  
        return result;
	}
}

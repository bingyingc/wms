package com.marlabs.Util;

import java.util.HashMap;
import java.util.Map;

import com.marlabs.Model.FrontSessionEntity;
import com.marlabs.Service.IUserService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;


/**
 *
 * 存放用户登录信息
 *
 * @author shamee-loop
 */
@Component
public class FrontSessionManager {

    /**
     * 用于存放客户端会话
     */
    private static Map<String, FrontSessionEntity> frontSessionMap = new HashMap<String, FrontSessionEntity>();

    @Value("${interface.type}")
    private String memcacheType;

    @Reference
    private IUserService userService;
    private static FrontSessionManager frontSessionManager;

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    private FrontSessionManager() {

    }

    // @PostConstruct
    // public void init() {
    // frontSessionManager = this;
    // frontSessionManager.userService = this.userService;
    //
    // }
    //
    // static{
    // initUserToken();
    // }
    //
    // /**
    // * 项目启动，默认加载全部的用户token信息，放置到缓存文件，避免服务应用重启导致前端用户需要重新登陆
    // */
    // private static void initUserToken(){
    // Map<String, Object> param = new HashMap<String, Object>();
    // List<User> userList = frontSessionManager.userService.queryList(param);
    // logger.info("获取到用户token列表，list.size={}", userList != null ?
    // userList.size() : 0);
    // if(userList != null && userList.size() > 0){
    // for(User user : userList){
    // /**
    // * 获取用户信息
    // */
    //
    // FrontSessionEntity entity = new FrontSessionEntity();
    // entity.setUser(user);
    // FrontSessionManager.setFrontSessionEntity(user.getToken(), entity);//
    // 将会员信息保存到session里
    // }
    // }
    // }

    /**
     * 设置前端session信息
     *
     * @param token
     * @param entity
     */
    public static void setFrontSessionEntity(String token,
                                             FrontSessionEntity entity) {
        frontSessionMap.put(token, entity);
    }

    /**
     * 移除前端session信息
     *
     * @param token
     */
    public static void removeFrontSessionEntity(String token) {
        frontSessionMap.remove(token);
    }

    /**
     * 根据token获取前端session信息
     *
     * @param token
     * @return
     */
    public static FrontSessionEntity getFrontSessionEntity(String token) {
        if (!TokenUtils.isValid(token)) {
            return null;
        }
        return frontSessionMap.get(token);
    }

    /**
     * 根据token获取用户信息，没有登录则为空
     *
     * @param token
     * @return
     */
    public static JSONObject getUser(String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (!TokenUtils.isValid(token)) {
                jsonObject.put("data", null);
                jsonObject.put("message", "系统登陆超时，请重新登陆。");
                jsonObject.put("code", Constants.RESP_INVLID_TOKEN);
                return jsonObject;
            }
            FrontSessionEntity f = null;
//			if (!StringUtils.isBlank(memcacheType)
//					&& memcacheType.trim().equals("test")) {
            if (frontSessionMap.get(token) == null) {
                jsonObject.put("data", null);
                jsonObject
                        .put("message",
                                "账号登录失效，请重新登录! ");
                jsonObject.put("code", Constants.RESP_INVLID_TOKEN);
                return jsonObject;
            }
            f = frontSessionMap.get(token);
//			} else {
//				if (MemcachedUtils.get(token) == null) {
//					jsonObject.put("data", null);
//					jsonObject.put("message",
//							"账号登录失效，请重新登录!");
//					jsonObject.put("code", Constants.RESP_INVLID_TOKEN);
//					return jsonObject;
//				}
//				f = (FrontSessionEntity) MemcachedUtils
//						.get(token);
//			}

            jsonObject.put("data", f.getUser());
            jsonObject.put("message", "OK");
            jsonObject.put("code", Constants.RESP_SUCCESS);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("data", null);
            jsonObject.put("message", "系统异常");
            jsonObject.put("code", Constants.RESP_SYS_ERROR);
            return jsonObject;
        }

    }

}

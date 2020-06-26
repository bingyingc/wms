package com.marlabs.Controller;

import com.marlabs.Model.*;
import com.marlabs.Redis.IUserDao;
import com.marlabs.Service.*;
import com.marlabs.Util.*;
import com.marlabs.Vo.UserListVo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlabs.Vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

@RestController
public class UserController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IUserTokenService userTokenService;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IPermissionService permissionService;
    @Value("${token.valid.time}")
    private String tokenValidTime;

    @Value("${interface.type}")
    private String  memcacheType;

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    @CrossOrigin(origins = "*")
    public JSONObject login(@RequestBody String reqInfo){
        String requestInfoStringDecode;
        String userName = null;
        String userType = null;
        String password = null;
        try {
            requestInfoStringDecode = java.net.URLDecoder.decode(reqInfo, "UTF-8");
            HashMap<String,Object> requestInfoMap =
                    new ObjectMapper().readValue(requestInfoStringDecode, HashMap.class);
            userName = (String) requestInfoMap.get("username");
            userType = (String) requestInfoMap.get("usertype");
            password = (String) requestInfoMap.get("password");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        logger.info("phone={}, password={}", userName, password);
        Map<String, Object> param = new HashMap<String, Object>();
        try {
            if(StringUtils.isBlank(userName)){
                return assemblyJson(null, Constants.RESP_PARAM_NULL_ERROR, "Please enter the user name");
            }
            if(StringUtils.isBlank(password)){
                return assemblyJson(null, Constants.RESP_PARAM_NULL_ERROR, "Please enter the password");
            }
                param.clear();
                param.put("userName", userName);
                param.put("password", password);
                param.put("isDelete", "0");
            List<User> list;
            list = userService.queryUser(param);
            logger.info("根据该用户名{}密码获取到用户列表大小list.size={}", userName, list != null ? list.size() : 0);

            if(list != null && list.size() > 0 && list.get(0) != null){
                User user = list.get(0);
                logger.info("登录成功");
                Date now=new Date();
                Date firstLogin=list.get(0).getFirstLogin();
                if (null==firstLogin) {
                    firstLogin=now;
                    user.setFirstLogin(firstLogin);
                    userService.updateByPrimaryKeySelective(user);
                }
                String token = TokenUtils.createToken();
                if(!StringUtils.isBlank(memcacheType) && memcacheType.trim().equals("test")){
                    db_token(token, list.get(0));

                } else {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("userId", list.get(0).getUserId());
                    params.put("isDelete", "0");
                    List<UserToken> tokenList = userTokenService.queryList(params);
                    logger.info("user token list:{}", tokenList != null ? tokenList.size() : 0);
                    String oldToken = "";
                    if (tokenList != null && tokenList.size() > 0) {// 更新
                        UserToken userToken = tokenList.get(0);
                        oldToken = userToken.getToken();
                        userToken.setToken(token);
                        userTokenService.updateByPrimaryKeySelective(userToken);
                    } else {
                        UserToken userToken = new UserToken();
                        userToken.setUserId(Long.parseLong(list.get(0).getUserId() + ""));
                        userToken.setToken(token);
                        userToken.setIsDelete("0");
                        userTokenService.insert(userToken);
                    }
                    param.clear();
                    param.put("token", token);
                    boolean result = userDao.add(user, param);
                    logger.info("----->"+result);
                    if(!StringUtils.isBlank(oldToken)){
                        result = userDao.detele(oldToken);
                        logger.info("----->"+result);
                    }
                    param.clear();
                }
                UserVo userVo = new UserVo();
                userVo.setUserId(user.getUserId());
                userVo.setUserName(user.getUserName());
                userVo.setToken(token);
                userVo.setPhone(user.getPhoneNum());
                //todo: menuList and permissonList
                param.clear();
                param.put("userId", user.getUserId());
                List<UserRole> userRoleList = userRoleService.queryList(param);
                List<Menu> menuList = menuService.queryByRoleId(userRoleList.get(0).getRoleId());
                List<Permission> permissionList =  permissionService.queryByRoleId(userRoleList.get(0).getRoleId());
                userVo.setMenuList(menuList);
                userVo.setPermissionList(permissionList);
                param.clear();
                param.put("userId", user.getUserId());
                param.put("isDelete", "0");
                List<UserToken> tList = userTokenService.queryList(param);
                if(tList == null || tList.size() == 0 || tList.get(0) == null){
                    logger.info("Failed to get 2.0 Token");
                    return assemblyJson(null, Constants.RESP_BUSS_ERROR, "Failed to get 2.0 Token");
                } else {
                    Long diff = new Date().getTime() - tList.get(0).getCreateTime().getTime();
                    userVo.setExpiresIn(Long.parseLong(tokenValidTime) * 3600 - diff/1000);//转成S

                    return assemblyJson(userVo, Constants.RESP_SUCCESS, "login successfully ");
                }

                    } else {
                        logger.info("Logon failed");
                        return assemblyJson(null, Constants.RESP_BUSS_ERROR, "Wrong password");
                    }

        } catch (Exception e) {
            logger.error("system error" + e.getMessage(), e);
            return assemblyJson(null, Constants.RESP_SYS_ERROR, "system error");
        }
    }

    /**
     * @param token
     * @param user
     */
    private void db_token(String token, User user){
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.clear();
        param.put("id", user.getUserId());
        param.put("isDelete", "0");
        List<UserToken> tokenList = userTokenService.queryList(param);

        if(tokenList != null && tokenList.size() > 0 && tokenList.get(0) != null){
            UserToken userToken = tokenList.get(0);
            String oldToken = userToken.getToken();
            userToken.setToken(token);
            userTokenService.updateByPrimaryKeySelective(userToken);

            FrontSessionEntity entity = new FrontSessionEntity();
            entity.setUser(user);


            FrontSessionManager.setFrontSessionEntity(token, entity);
            FrontSessionManager.removeFrontSessionEntity(oldToken);

        } else {
            //新增
            UserToken userToken = new UserToken();
            userToken.setUserId(Long.parseLong(user.getUserId() + ""));
            userToken.setIsDelete("0");
            userToken.setToken(token);
            userTokenService.insert(userToken);

            FrontSessionEntity entity = new FrontSessionEntity();
            entity.setUser(user);

            FrontSessionManager.setFrontSessionEntity(token, entity);
        }

    }

    @RequestMapping(value = "/userList", method = {RequestMethod.POST})
    @ResponseBody
    @CrossOrigin(origins = "*")
    public UserListVo geUserList(@RequestBody String requestInfoString) {
        List<User> userList =  new ArrayList<>();
        UserListVo userListVo = new UserListVo();
        String requestInfoStringDecode = null;
        Map<String, Object> param = new HashMap<String, Object>();
        int total = 0;
        try {
            requestInfoStringDecode = java.net.URLDecoder.decode(requestInfoString, "UTF-8");
            HashMap<String,Object> requestInfoMap =
                    new ObjectMapper().readValue(requestInfoStringDecode, HashMap.class);
            String nameLk = (String) requestInfoMap.get("nameLk");
            Integer pageSize = (Integer) requestInfoMap.get("pageSize");
            Integer pageNumber = (Integer) requestInfoMap.get("pageNumber");
            Integer pageOffset = pageNumber * pageSize;
            param.clear();
            param.put("pageSize", pageSize);
            param.put("userName", nameLk);
            param.put("isDelete", "0");
            param.put("pageOffset",pageOffset);
            userList = userService.queryList(param);
            total = userService.queryByCount(param);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        userListVo.setUserList(userList);
        userListVo.setTotal(total);
        return userListVo;
    }
}


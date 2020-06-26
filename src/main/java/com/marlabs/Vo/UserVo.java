package com.marlabs.Vo;

import com.marlabs.Model.Menu;
import com.marlabs.Model.Permission;

import java.util.List;

/**
 * 用于接口组装数据
 *
 * @author shamee-loop
 *
 */
public class UserVo {

    /**
     *
     */

    /** 2.0token数据 * */
    private String token;
    private Long expiresIn;// 失效时间（单位：s）

    /** 2.0token数据 * */
    private String oldToken;
    private String userName;
    private String phone;
    private List<Menu> menuList;
    private List<Permission> permissionList;
    private long userId;


    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getOldToken() {
        return oldToken;
    }

    public void setOldToken(String oldToken) {
        this.oldToken = oldToken;
    }

}


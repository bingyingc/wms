package com.marlabs.Model;

import java.sql.Timestamp;
import java.util.Date;

@SuppressWarnings("serial")
public class UserToken extends BaseDomain {


    private Long id;//
    private Long userId;//   关联用户id
    private String token;//   用户登录token
    private Date createTime;//
    private String isDelete;//   0未删除  1已删除
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id=id;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId=userId;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token=token;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIsDelete() {
        return this.isDelete;
    }
    public void setIsDelete(String isDelete) {
        this.isDelete=isDelete;
    }
}


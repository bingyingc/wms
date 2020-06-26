package com.marlabs.Vo;

import com.marlabs.Model.Role;

import java.util.List;

public class RoleListVo {
    private List<Role> roleList;
    private int total;

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

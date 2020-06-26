package com.marlabs.Model;

public class RolePermission {
    private String roleId;

    private String permiId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getPermiId() {
        return permiId;
    }

    public void setPermiId(String permiId) {
        this.permiId = permiId == null ? null : permiId.trim();
    }
}
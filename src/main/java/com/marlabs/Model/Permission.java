package com.marlabs.Model;

public class Permission {
    private String permiId;

    private String permiName;

    private String description;

    private String httpPath;

    //=============join role_permission table
    private Long roleId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getPermiId() {
        return permiId;
    }

    public void setPermiId(String permiId) {
        this.permiId = permiId == null ? null : permiId.trim();
    }

    public String getPermiName() {
        return permiName;
    }

    public void setPermiName(String permiName) {
        this.permiName = permiName == null ? null : permiName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String httpPath) {
        this.httpPath = httpPath == null ? null : httpPath.trim();
    }
}
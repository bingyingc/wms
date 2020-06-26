package com.marlabs.Service;

import com.marlabs.Model.Permission;

import java.util.List;

public interface IPermissionService extends IBaseService<Permission> {
    List<Permission> queryByRoleId(Long roleId);
}

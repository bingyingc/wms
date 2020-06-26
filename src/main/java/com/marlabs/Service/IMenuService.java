package com.marlabs.Service;

import com.marlabs.Model.Menu;

import java.util.List;

public interface IMenuService extends IBaseService<Menu> {
    List<Menu> queryByRoleId(Long roleId);
}

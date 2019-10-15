package com.itheima.dao;

import com.itheima.domain.pojo.Menu;

import java.util.List;

public interface MenuMapper {
    /**
     * 动态获取菜单
     * @return
     * @param username
     */
    List<Menu> findMenu(String username);

    List<Menu> findParentMenuId(Integer id);
}

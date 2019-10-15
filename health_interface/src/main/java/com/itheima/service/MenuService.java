package com.itheima.service;

import com.itheima.domain.pojo.Menu;

import java.util.List;

public interface MenuService {
    /**
     * 动态获取菜单
     * @return
     * @param username
     */
    List<Menu> findMenu(String username);
}

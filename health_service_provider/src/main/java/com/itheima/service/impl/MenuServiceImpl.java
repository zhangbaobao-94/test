package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MenuMapper;
import com.itheima.domain.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    /**
     * 动态获取菜单
     *
     * @return
     * @param username
     */
    @Override
    public List<Menu> findMenu(String username) {
        return menuMapper.findMenu(username);
    }
}

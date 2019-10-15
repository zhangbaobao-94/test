package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.constant.MessageConstant;
import com.itheima.domain.enity.Result;
import com.itheima.domain.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    /**
     * 动态获取菜单
     * @return
     */
    @RequestMapping("/getMenu")
    public Result getMenu(String username) {
        try {
            List<Menu> list = menuService.findMenu(username);

            return new Result(true, MessageConstant.GET_MENU_SUCCESS,list);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
    }
}

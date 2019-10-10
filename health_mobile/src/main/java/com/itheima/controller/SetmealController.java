package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.constant.MessageConstant;
import com.itheima.domain.enity.Result;
import com.itheima.domain.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有的套餐信息,不分页,不条件查询
     *
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Setmeal> list = setmealService.findAll();
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 多表查询,根据套餐id查询检查组,检查项
     *
     * @param id
     * @return
     */
    @RequestMapping("/findAllById")
    public Result findAllById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findAllById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 根据套餐的id单表查询套餐的基本信息
     * @param id
     * @return
     */
    @RequestMapping("/findBySetmealId")
    public Result findBySetmealId(Integer id) {
        try {
            Setmeal setmeal = setmealService.findBySetmealId(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}

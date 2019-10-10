package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.constant.MessageConstant;
import com.itheima.domain.enity.PageResult;
import com.itheima.domain.enity.QueryPageBean;
import com.itheima.domain.enity.Result;
import com.itheima.domain.pojo.CheckItem;
import com.itheima.service.ItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Reference
    private ItemService itemService;

    //添加数据
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            itemService.add(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }

    //按照条件进行分页查询
    @RequestMapping("/findByCondition")
    public PageResult findByCondition(@RequestBody QueryPageBean queryPageBean) {
        return itemService.findByCondition(queryPageBean);
    }

    //根据id删除对象
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id) {
        try {
            itemService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    //通过id进行查询
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            CheckItem checkItem = itemService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //更新数据
    @RequestMapping("/update")
    public Result update(@RequestBody CheckItem checkItem) {
        try {
            itemService.update(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    //查询所有的检查项数据
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<CheckItem> list = itemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}

package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.constant.MessageConstant;
import com.itheima.domain.enity.PageResult;
import com.itheima.domain.enity.QueryPageBean;
import com.itheima.domain.enity.Result;
import com.itheima.domain.pojo.CheckGroup;
import com.itheima.service.GroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Reference
    private GroupService groupService;

    //添加检查组
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            groupService.add(checkGroup, checkitemIds);

            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    //分页条件查询
    @RequestMapping("/page")
    public PageResult page(@RequestBody QueryPageBean queryPageBean) {
        return groupService.page(queryPageBean);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map<String, Object> map = groupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 更新检查组对象
     *
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            groupService.update(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 查询所有的检查组
     *
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<CheckGroup> list = groupService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 检查组的删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            groupService.delete(id);
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }
}

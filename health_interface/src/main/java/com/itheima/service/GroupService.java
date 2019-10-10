package com.itheima.service;

import com.itheima.domain.enity.PageResult;
import com.itheima.domain.enity.QueryPageBean;
import com.itheima.domain.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface GroupService {
    //新增检查组
    void add(CheckGroup checkGroup, Integer[] checkItemIds);

    //分页条件查询
    PageResult page(QueryPageBean queryPageBean);

    /**
     * 通过id进行查询
     *
     * @param id
     * @return
     */
    Map<String, Object> findById(Integer id);

    /**
     * 查询所有的检查组
     * @return
     */
    List<CheckGroup> findAll();

    /**
     * 更新检查组信息
     * @param checkGroup
     * @param checkitemIds
     */
    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 检查组的删除
     * @param id
     */
    void delete(Integer id);
}

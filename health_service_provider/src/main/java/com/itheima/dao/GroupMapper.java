package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.domain.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface GroupMapper {
    //在t_checkgroup表中添加数据
    void addCheckGroup(CheckGroup checkGroup);


    //在中间表t_checkgroup_checkitem添加数据
    void addCheckGroup_Item(List<Map<String, Object>> list);

    //条件查询
    Page<CheckGroup> findByName(String queryString);

    /**
     * 通过id查询checkgroup对象
     *
     * @param id
     * @return
     */
    CheckGroup findCheckGroupById(Integer id);

    /**
     * 通过id查询中间表的checkitem_id的值
     *
     * @param id
     * @return
     */
    Integer[] findCheckItemsById(Integer id);

    /**
     * 查询所有的检查组
     *
     * @return
     */
    List<CheckGroup> findAll();

    /**
     * t_checkgroup表格的更新
     *
     * @param checkGroup
     */
    void updateCheckGroup(CheckGroup checkGroup);

    /**
     * 根据checkGroup的id值,删除对应的所有checkItem的id值
     * @param id
     */
    void deleteCheckItemIds(Integer id);

    /**
     * 删除表t_checkgroup对象
     * @param id
     */
    void deleteCheckGroup(Integer id);

    /**
     * 根据检查套餐的id查询检查组列表
     * @param id
     * @return
     */
    List<CheckGroup> findById(Integer id);
}

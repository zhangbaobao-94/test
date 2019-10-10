package com.itheima.dao;

import com.itheima.domain.pojo.CheckItem;

import java.util.List;

public interface ItemMapper {
    //添加数据
    void add(CheckItem checkItem);

    //按照条件进行查询
    List<CheckItem> findByName(String queryString);

    //按照id进行删除
    void deleteById(Integer id);

    //通过id进行查询
    CheckItem findById(Integer id);

    //更新对象
    void update(CheckItem checkItem);

    //根据checkitem的id在检查组和检查项的中间表进行查询检查项的个数
    int findGroupIdCount(Integer id);

    //查询所有的checkitem对象
    List<CheckItem> findAll();

    /**
     * 根据检查组的id查询检查项
     * @param id
     * @return
     */
    List<CheckItem> findByGroupId(Integer id);
}


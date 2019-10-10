package com.itheima.service;

import com.itheima.domain.enity.PageResult;
import com.itheima.domain.enity.QueryPageBean;
import com.itheima.domain.pojo.CheckItem;

import java.util.List;

public interface ItemService {
    //添加操作
    void add(CheckItem checkItem);

    //按照条件进行分页查询
    PageResult findByCondition(QueryPageBean queryPageBean);

    //按照id进行删除
    void deleteById(Integer id);

    //通过id进行查询
    CheckItem findById(Integer id);

    //更新对象
    void update(CheckItem checkItem);

    //查询所有的checkitem对象
    List<CheckItem> findAll();

}

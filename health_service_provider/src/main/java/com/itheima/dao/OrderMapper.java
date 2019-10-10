package com.itheima.dao;

import com.itheima.domain.enity.Result;
import com.itheima.domain.pojo.Order;

import java.util.Map;

public interface OrderMapper {
    /**
     * 提交预约功能
     * @param map
     * @return
     */
    Result submit(Map map);

    Map findById(Integer id);

    /**
     * 根据orderDate,setmealId,member_Id判断是否已经预约
     * @param order
     * @return
     */
    long isOrder(Order order);

    /**
     * 预约操作,即在t_order中添加对象
     * @param order
     */
    void add(Order order);

    /**
     * 根据order表的id值查询相关联表的值
     * @param id
     * @return
     */
    Map findAllBy(Integer id);
}

package com.itheima.dao;

import com.itheima.domain.enity.Result;
import com.itheima.domain.pojo.Order;

import java.util.Date;
import java.util.List;
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

    /**
     * 今日预约人数
     * @return
     */
    Integer todayOrder();

    /**
     * 本周预约数
     * @return
     * @param firstDayOfWeek
     */
    Integer weekOrder(String firstDayOfWeek);

    /**
     * 本月预约人数
     * @return
     */
    Integer monthOrder();

    /**
     * 今日到诊人数
     * @return
     */
    Integer orderStatus();

    /**
     * 本周到诊人数
     * @return
     * @param firstDayOfWeek
     */
    Integer weekStatus(String firstDayOfWeek);

    /**
     * 本月到诊人数
     * @return
     */
    Integer monthStatus();

    /**
     * 热门套餐
     * @return
     */
    List<Map<String,Object>> getHotSetmeal();
}

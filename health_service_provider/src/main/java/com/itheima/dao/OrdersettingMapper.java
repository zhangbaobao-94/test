package com.itheima.dao;

import com.itheima.domain.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrdersettingMapper {
    /**
     * 根据日期字段在表格中查找
     *
     * @param orderDate
     * @return
     */
    List<OrderSetting> findByOrderDate(Date orderDate);

    /**
     * 更新数据
     *
     * @param orderSetting
     */
    void update(OrderSetting orderSetting);

    /**
     * 添加数据
     *
     * @param orderSetting
     */
    void insert(OrderSetting orderSetting);

    List<OrderSetting> findByDate(Map<String, Object> map);

    /**
     * 根据orderDate查询ordersetting对象
     *
     * @param date
     * @return
     */
    OrderSetting findDate(Date date);

    /**
     * 预约成功后,更新表格,即reservations+1
     * @param orderSetting
     */
    void updateReservations(OrderSetting orderSetting);
}

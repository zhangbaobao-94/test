package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrdersettingMapper;
import com.itheima.domain.pojo.OrderSetting;
import com.itheima.service.OrdersettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrdersettingService.class)
@Transactional
public class OrdersettingServiceImpl implements OrdersettingService{
    @Autowired
    private OrdersettingMapper ordersettingMapper;

    /**
     * 把集合中的数据添加到t_ordersetting表中
     *
     * @param orderSettingList
     */
    @Override
    public void saveFile(List<OrderSetting> orderSettingList) {
        for (OrderSetting orderSetting : orderSettingList) {
            //如果对象中的日期和表格中重复,就采用update方法
            List<OrderSetting> ordersetting_table = ordersettingMapper.findByOrderDate(orderSetting.getOrderDate());
            if (ordersetting_table != null && ordersetting_table.size() > 0) {
                //如果添加的数据日期,数据库中已存在,进行更新
                ordersettingMapper.update(orderSetting);
            } else {
                //如果与表格中的日期不重复,直接采用添加方法
                ordersettingMapper.insert(orderSetting);
            }
        }

    }

    @Override
    public List<Map<String, Object>> getOrderSettingByMonth(String date) {
        //进行日期的拼接
        String start = date + "-1";
        String end = date + "-31";
        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);
        List<OrderSetting> list = ordersettingMapper.findByDate(map);
        List<Map<String, Object>> list1 = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String, Object> data = new HashMap<>();
            data.put("date", orderSetting.getOrderDate().getDate());
            data.put("number", orderSetting.getNumber());
            data.put("reservations", orderSetting.getReservations());
            list1.add(data);
        }
        return list1;
    }

    /**
     * 单独修改可预约人数
     *
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        List<OrderSetting> list = ordersettingMapper.findByOrderDate(orderSetting.getOrderDate());
        if (list != null && list.size() > 0) {
            ordersettingMapper.update(orderSetting);
        } else {
            ordersettingMapper.insert(orderSetting);
        }
    }
}

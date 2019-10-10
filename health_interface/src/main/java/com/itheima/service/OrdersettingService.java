package com.itheima.service;

import com.itheima.domain.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrdersettingService {
    /**
     * 在t_ordersetting表中添加数据
     *
     * @param orderSettingList
     */
    void saveFile(List<OrderSetting> orderSettingList);

    /**
     * 批量导入预约
     * @param date
     * @return
     */
    List<Map<String, Object>> getOrderSettingByMonth(String date);

    /**
     * 单独修改可预约人数
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting);


}

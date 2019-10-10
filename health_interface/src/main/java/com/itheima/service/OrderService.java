package com.itheima.service;

import com.itheima.domain.enity.Result;

import java.util.Map;

public interface OrderService {
    /**
     * 根据order表的id值查询相关联表的值
     * @param id
     * @return
     */
    public Map findById(Integer id);

    /**
     * 提交预约功能
     * @param map
     * @return
     */
    public Result submit(Map map) throws Exception;
}

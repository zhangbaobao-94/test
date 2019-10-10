package com.itheima.service;

import com.itheima.domain.enity.PageResult;
import com.itheima.domain.enity.QueryPageBean;
import com.itheima.domain.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    /**
     * 新建选项
     * @param setmeal
     * @param checkgroupIds
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    PageResult page(QueryPageBean queryPageBean);

    /**
     * 删除检查套餐
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据id查询检查套餐
     * @param id
     * @return
     */
    Map<String, Object> findById(Integer id);

    /**
     * 检查套餐的编辑
     * @param setmeal
     * @param checkgroupIds
     */
    void update(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 查询所有的检查套餐信息
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 多表查询,根据套餐id查询检查组,检查项
     * @param id
     * @return
     */
    Setmeal findAllById(Integer id);

    /**
     * 根据套餐的id单表查询套餐的基本信息
     * @param id
     * @return
     */
    Setmeal findBySetmealId(Integer id);
}

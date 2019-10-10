package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.domain.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealMapper {
    /**
     * 检查套餐的添加
     *
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 在检查套餐和检查组的中间表中添加数据
     *
     * @param list
     */
    void insertSetmealAndCheckGroup(List<Map<String, Object>> list);

    /**
     * 检查套餐的条件查询
     *
     * @param queryString
     * @return
     */
    Page<Setmeal> findByCondition(String queryString);

    /**
     * 删除中间表
     *
     * @param id
     */
    void deleteSetmealAndCheckgroup(Integer id);

    /**
     * 删除t_setmeal表
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过id查询setmeal对象
     *
     * @param id
     * @return
     */
    Setmeal findSetmeal(Integer id);

    /**
     * 根据id查询对应的中间表
     *
     * @param id
     * @return
     */
    Integer[] findIds(Integer id);

    /**
     * 更新t_setmeal表格
     *
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 查询所有的套餐检查信息
     *
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 多表查询,根据套餐id查询检查组,检查项
     *
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

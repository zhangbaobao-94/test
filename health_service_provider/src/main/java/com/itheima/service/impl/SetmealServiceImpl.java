package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.SetmealMapper;
import com.itheima.domain.constant.RedisConstant;
import com.itheima.domain.enity.PageResult;
import com.itheima.domain.enity.QueryPageBean;
import com.itheima.domain.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 检查套餐表的添加
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealMapper.add(setmeal);
        if (setmeal.getImg() != null) {
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        }
        Integer setmealId = setmeal.getId();
        this.setTableSetmealAndCheckGroup(setmealId, checkgroupIds);

    }

    /**
     * 分页条件查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult page(QueryPageBean queryPageBean) {
        if (queryPageBean == null) {
            queryPageBean = new QueryPageBean();
        }
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealMapper.findByCondition(queryString);
        long total = page.getTotal();
        List<Setmeal> result = page.getResult();

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 删除检查套餐
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //先删除中间表
        setmealMapper.deleteSetmealAndCheckgroup(id);
        //在删除t_setmeal表中的对象
        setmealMapper.delete(id);
    }

    /**
     * 根据id查询检查套餐
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findById(Integer id) {
        Setmeal setmeal = setmealMapper.findSetmeal(id);
        Integer[] ids = setmealMapper.findIds(id);
        Map<String, Object> map = new HashMap<>();
        map.put("setmeal", setmeal);
        map.put("ids", ids);
        return map;
    }

    /**
     * 检查套餐的编辑
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        //编辑t_setmeal表格
        setmealMapper.update(setmeal);
        //删除中间表
        setmealMapper.deleteSetmealAndCheckgroup(setmeal.getId());
        //往中间表添加数据
        this.setTableSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
    }

    /**
     * 查询所有的检查套餐信息
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealMapper.findAll();
    }

    /**
     * 多表查询,根据套餐id查询检查组,检查项
     * @param id
     * @return
     */
    @Override
    public Setmeal findAllById(Integer id) {
        return setmealMapper.findAllById(id);
    }

    /**
     * 根据套餐的id单表查询套餐的基本信息
     * @param id
     * @return
     */
    @Override
    public Setmeal findBySetmealId(Integer id) {
        return setmealMapper.findBySetmealId(id);
    }

    /**
     * 在检查套餐和检查组的中间表添加值
     *
     * @param setmealId
     * @param checkgroupIds
     */
    private void setTableSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Object> map = new HashMap<>();
                map.put("setmealId", setmealId);
                map.put("checkgroupIds", checkgroupId);
                list.add(map);
            }
            setmealMapper.insertSetmealAndCheckGroup(list);
        }
    }
}

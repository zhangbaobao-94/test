package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.ItemMapper;
import com.itheima.domain.enity.PageResult;
import com.itheima.domain.enity.QueryPageBean;
import com.itheima.domain.pojo.CheckItem;
import com.itheima.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = ItemService.class)
@Transactional
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemMapper itemMapper;

    //新增数据
    @Override
    public void add(CheckItem checkItem) {
        itemMapper.add(checkItem);
    }

    //按照条件进行分页查询
    @Override
    public PageResult findByCondition(QueryPageBean queryPageBean) {
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
        //创建分页助手
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = (Page<CheckItem>) itemMapper.findByName(queryString);
        //获取总页数
        long total = page.getTotal();
        //获取当前页面
        List<CheckItem> result = page.getResult();
        return new PageResult(total, result);
    }

    //按照id进行删除
    @Override
    public void deleteById(Integer id) {
        //根据checkitem的id在检查组和检查项的中间表进行查询检查项的个数
        int count = itemMapper.findGroupIdCount(id);
        if (count > 0) {
            new RuntimeException();
        }
        itemMapper.deleteById(id);
    }

    //通过id进行查询
    @Override
    public CheckItem findById(Integer id) {
        return itemMapper.findById(id);
    }

    //更新对象
    @Override
    public void update(CheckItem checkItem) {
        itemMapper.update(checkItem);
    }

    //查询所有的checkitem对象
    @Override
    public List<CheckItem> findAll() {
        return itemMapper.findAll();
    }


}

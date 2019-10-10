package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.GroupMapper;
import com.itheima.domain.enity.PageResult;
import com.itheima.domain.enity.QueryPageBean;
import com.itheima.domain.pojo.CheckGroup;
import com.itheima.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = GroupService.class)
@Transactional
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupMapper groupMapper;


    //新增检查组
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        //获取t_checkgroup表中最后一次添加数据的id值
        groupMapper.addCheckGroup(checkGroup);
        Integer id = checkGroup.getId();
        this.setGroupAndItem(id, checkItemIds);
    }

    //分页条件查询
    @Override
    public PageResult page(QueryPageBean queryPageBean) {
        if (queryPageBean == null) {
            queryPageBean = new QueryPageBean();
        }
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = groupMapper.findByName(queryString);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 通过id查询检查组以及对应的检查项
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findById(Integer id) {
        CheckGroup checkGroup = groupMapper.findCheckGroupById(id);
        Integer[] checkItemIds = groupMapper.findCheckItemsById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("checkGrouop", checkGroup);
        map.put("checkItemIds", checkItemIds);
        return map;
    }

    /**
     * 查询所有的检查组
     *
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return groupMapper.findAll();
    }

    /**
     * 更新检查组信息
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //更新t_checkgroup表
        groupMapper.updateCheckGroup(checkGroup);
        //先把中间表中checkItemIds的数据删除
        groupMapper.deleteCheckItemIds(checkGroup.getId());
        //添加中间表中checkItemIds的数据
        this.setGroupAndItem(checkGroup.getId(), checkitemIds);
    }

    /**
     * 检查组的删除
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //先删除中间表
        groupMapper.deleteCheckItemIds(id);
        //在删除t_checkgroup表
        groupMapper.deleteCheckGroup(id);
    }

    //新增中间表
    public void setGroupAndItem(Integer groupId, Integer[] checkItemIds) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (checkItemIds != null && checkItemIds.length > 0) {
            //往中间表t_checkgroup_checkitem添加数据
            for (Integer checkItemId : checkItemIds) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("checkgroup_id", groupId);
                map.put("checkitem_id", checkItemId);
                list.add(map);
//                groupMapper.addCheckGroup_Item(map);
            }
            //往中间表t_checkgroup_checkitem添加数据
            groupMapper.addCheckGroup_Item(list);
        }
    }
}

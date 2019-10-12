package com.itheima.dao;

import com.itheima.domain.pojo.User;

public interface UserMapper {


    /**
     * 根据用户名查询对象
     * @param username
     * @return
     */
    User findByUsername(String username);
}

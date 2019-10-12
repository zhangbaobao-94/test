package com.itheima.service;

import com.itheima.domain.pojo.User;

public interface UserService {

    /**
     * 根据用户名查询对象
     * @param username
     * @return
     */
    User findByUsername(String username);
}

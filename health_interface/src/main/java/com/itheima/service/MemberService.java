package com.itheima.service;

import com.itheima.domain.pojo.Member;

public interface MemberService {
    /**
     * 根据手机号查询对象
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加对象
     * @param member
     */
    void add(Member member);
}

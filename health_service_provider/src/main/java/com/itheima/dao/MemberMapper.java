package com.itheima.dao;

import com.itheima.domain.pojo.Member;

public interface MemberMapper {
    /**
     * 根据手机号查询对象
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加会员
     * @param member_register
     */
    void add(Member member_register);
}

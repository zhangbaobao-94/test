package com.itheima.dao;

import com.itheima.domain.pojo.Member;

import java.util.Date;

public interface MemberMapper {
    /**
     * 根据手机号查询对象
     *
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加会员
     *
     * @param member_register
     */
    void add(Member member_register);

    /**
     * 获取每个月的会员数
     *
     * @param date
     * @return
     */
    Integer getMemberReport(String date);

    /**
     * 当天新增会员数
     *
     * @return
     */
    Integer todayAdd();

    /**
     * 本周新增会员数
     *
     * @return
     * @param firstDayOfWeek
     */
    Integer weekAdd(String firstDayOfWeek);

    /**
     * 总会员数
     * @return
     */
    Integer countMember();

    /**
     * 本月新增会员数
     * @return
     */
    Integer monthAdd();
}

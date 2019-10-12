package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberMapper;
import com.itheima.domain.pojo.Member;
import com.itheima.domain.utils.MD5Utils;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    /**
     * 根据手机号查询对象
     *
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberMapper.findByTelephone(telephone);
    }

    /**
     * 添加对象
     *
     * @param member
     */
    @Override
    public void add(Member member) {
        //获取密码
        String password = member.getPassword();
        //如果不为空,用MD5进行加密存储
        if (password != null) {
            String s = MD5Utils.md5(password);
            member.setPassword(s);
        }
        memberMapper.add(member);
    }

    /**
     * 会员数量折线图
     *
     * @return
     */
    @Override
    public Map<String, Object> getMemberReport() {
        Map<String, Object> map = new HashMap<>();
        List<String> months = new ArrayList<>();
        List<Integer> memberCount = new ArrayList<>();
        //查询每个月的会员数量
        Calendar calendar = Calendar.getInstance();
        //将日期推前12个月
        calendar.add(calendar.MONTH, -12);
        for (int i = 0; i < 12; i++) {
            calendar.add(calendar.MONTH, 1);
            String date = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
            months.add(date);
            String date1 = date + "-31" ;
           Integer memberReport = memberMapper.getMemberReport(date1);
            memberCount.add(memberReport);
        }
        map.put("months", months);
        map.put("memberCount", memberCount);
        return map;
    }


}

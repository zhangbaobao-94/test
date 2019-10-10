package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.domain.constant.MessageConstant;
import com.itheima.domain.constant.RedisMessageConstant;
import com.itheima.domain.enity.Result;
import com.itheima.domain.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Reference
    MemberService memberService;
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map) {
        try {
            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");
            //从Redis中获取验证码
            String redis_code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
            //比较验证码是否正确
            if (redis_code == null || validateCode == null || !validateCode.equals(redis_code)) {
                return new Result(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
            }
            //正确,根据手机号查询t_member表格,返回对象
            Member member = memberService.findByTelephone(telephone);

            //为空,进行添加
            if (member == null) {
                member = new Member();
                //添加对象
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }

            //把对象添加到cookie中
            Cookie cookie = new Cookie("telephone", telephone);
            //持久化cookie
            cookie.setMaxAge(60*60*24*30);
            //设置路径,默认Tomcat服务器,cookie不能共享,设置路径为"/",实现共享
            cookie.setPath("/");
            //保存cookie
            response.addCookie(cookie);
            //把对象以json字符串格式存储到Jedis中,30分钟
            String  member_redis= JSON.toJSONString(member);
            jedisPool.getResource().setex(telephone, 60 * 30, member_redis);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
        }
    }
}

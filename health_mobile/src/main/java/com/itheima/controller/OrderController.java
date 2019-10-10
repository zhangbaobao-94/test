package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.constant.MessageConstant;
import com.itheima.domain.constant.RedisMessageConstant;
import com.itheima.domain.enity.Result;
import com.itheima.domain.pojo.Order;
import com.itheima.domain.utils.SMSUtils;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 提交预约功能
     *
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        //从集合map中获取验证码,手机号,预约时间
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String orderDate = (String) map.get("orderDate");

        Result result = null;
        //从Redis中获取验证码
        try {
            String validateCode_redis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            //先验证验证码是否正确
            if (validateCode_redis == null || !validateCode_redis.equals(validateCode) || validateCode == null) {
                //错误
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            //验证码正确
            //给map赋值一个orderType,即微信预约
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            //调用service,返回一个result结果,
            result = orderService.submit(map);
            //判断isFlag正确,无误,发送预约成功信息
            if (result.isFlag()) {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDate);
                //成功发完短信后,把Redis中的短信验证码进行删除操作,加强代码的健壮性
                jedisPool.getResource().del(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据order表的id值查询相关联表的值
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}




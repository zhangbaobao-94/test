package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.domain.constant.MessageConstant;
import com.itheima.domain.constant.RedisMessageConstant;
import com.itheima.domain.enity.Result;
import com.itheima.domain.utils.SMSUtils;
import com.itheima.domain.utils.ValidateCodeUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约功能,发送验证短信
     *
     * @param telephone
     * @return
     */
    @RequestMapping("/sendCheckCode")
    public Result sendCheckCode(String telephone) {
        //对前台发送的手机号进行正则校验
        String regEx = "^1+[35678]+\\d{9}";
        boolean matches = telephone.matches(regEx);
        if (!matches) {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //获取随机生成的4位数字验证码,用提供的工具类
//        Integer checkCode = ValidateCodeUtils.generateValidateCode(4);
        //用common-lang包自带的工具列
        String checkCode = RandomStringUtils.randomNumeric(4);

        //发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, checkCode.toString());
            //发送成功后,把验证码存储到Redis中,考虑到存储的时间问题
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, checkCode.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 快速登录时,发送验证码
     *
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //对前台发送的手机号进行校验
        String regEx = "^1+[35678]+\\d{9}";
        boolean matches = regEx.matches(telephone);
        if (!matches) {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //生成6位的验证码
        try {
            Integer integer = ValidateCodeUtils.generateValidateCode(6);
            //调用工具类发送短信
            SMSUtils.sendShortMessage(SMSUtils.LOGIN_NOTICE, telephone, integer.toString());
            //把验证码存储到Redis中,存半个小时
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 30 * 60, integer.toString());
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}

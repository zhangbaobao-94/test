package com.itheima.controller;

import com.itheima.domain.constant.MessageConstant;
import com.itheima.domain.enity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取登录的用户名
     *
     * @return
     */
    @RequestMapping("/getName")
    public Result getName() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user != null) {
                return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
            }
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    /**
     * 获取登录页面的错误信息,并展示在页面上
     * @return
     */
    @RequestMapping("/getErrorMessage")
    public String getErrorMessage(HttpSession session) {
        //从session域中获取异常信息
        Exception exception = (Exception)session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        String message = exception.getMessage();
        if (message != null&&message.equals("密码错误,请输入正确的密码")) {
            return message;
        }else if(message.equals("UserDetailsService returned null, which is an interface contract violation")) {
            return "用户名错误";
        }
        return null;
    }
}

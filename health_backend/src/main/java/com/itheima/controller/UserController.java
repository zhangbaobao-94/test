package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.pojo.Person;
import com.itheima.domain.enity.Result;
import com.itheima.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//@RestController 相当于
@Controller
@RequestMapping("/person")
public class UserController {
    @Reference
    private UserService userService;

    @RequestMapping("/findAll")
    @ResponseBody
    public Result findAll() {
        List<Person> persons = userService.findAll();
        System.out.println(persons);
        Result result = new Result();
        try {
            persons = userService.findAll();
            result = new Result(true, "查询成功", persons);
        } catch (Exception e) {
            result = new Result(false, "查询失败", null);
            e.printStackTrace();
        }
        return result;
    }
}

package com.itheima.dao;

import com.itheima.domain.pojo.Person;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    //查询所有的对象
    @Select("select * from person")
    List<Person> findAll();
}

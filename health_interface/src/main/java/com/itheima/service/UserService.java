package com.itheima.service;

import com.itheima.domain.pojo.Person;

import java.util.List;

public interface UserService {
    List<Person> findAll();
}

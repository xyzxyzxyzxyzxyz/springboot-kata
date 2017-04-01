package com.tdd.katas.springboot.services;

import com.tdd.katas.springboot.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findOne(long l);
}

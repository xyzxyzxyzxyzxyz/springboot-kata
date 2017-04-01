package com.tdd.katas.springboot.services;

import com.tdd.katas.springboot.model.User;
import com.tdd.katas.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return (List<User>) this.userRepository.findAll();
    }

    @Override
    public User findOne(long userId) {
        return this.userRepository.findOne(userId);
    }
}

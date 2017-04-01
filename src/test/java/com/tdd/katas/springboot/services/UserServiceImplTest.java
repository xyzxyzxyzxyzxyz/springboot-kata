package com.tdd.katas.springboot.services;

import com.tdd.katas.springboot.model.User;
import com.tdd.katas.springboot.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp(){
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void when_FindAll_Retrieve_List_Of_Users() throws Exception {

        List<User> userList = new ArrayList<>();
        userList.add(new User(1L,"user1","password", "email@email.com"));

        given(userRepository.findAll()).willReturn(userList);

        assertEquals("The returned user list is as expected ", userList, userService.findAll());
    }
}

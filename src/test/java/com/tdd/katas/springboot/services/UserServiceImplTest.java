package com.tdd.katas.springboot.services;

import com.tdd.katas.springboot.model.User;
import com.tdd.katas.springboot.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private UserService userService;

    @Mock
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

    @Test
    public void when_FindOne_Retrieve_Expected_User() throws Exception {
        User original = new User(1L,"user1","password", "email@email.com");
        given(userRepository.findOne(1L)).willReturn(original);

        User expected = new User(1L,"new_user1","new_password", "new_email@email.com");
        assertEquals("The returned user is as expected ", expected, userService.findOne(1L));

    }

    @Test
    public void when_SaveNewUser_The_User_Is_Saved() throws Exception {
        User userToBeSaved = new User(null,"user1","password", "email@email.com");
        User expected = new User(1L,"user1","password", "email@email.com");

        given(userRepository.save(userToBeSaved)).willReturn(expected);

        User savedUser = userService.save(userToBeSaved);

        verify(userRepository).save(userToBeSaved);
        assertNotNull("The returned saved user id is not null ", savedUser.getId());
        assertEquals("The returned saved user login is equals", userToBeSaved.getLogin(), savedUser.getLogin());
    }


}

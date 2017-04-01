package com.tdd.katas.springboot.services;

import com.tdd.katas.springboot.model.User;
import com.tdd.katas.springboot.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

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

        List<User> expected = new ArrayList<>();
        expected.add(new User(1L,"user1","password", "email@email.com"));

        given(userRepository.findAll()).willReturn(expected);

        List<User> actual = userService.findAll();

        verify(userRepository).findAll();
        assertTrue("The user list is returned as is from the repository", expected == actual);
    }

    @Test
    public void when_FindOne_Retrieve_Expected_User() throws Exception {
        User expected = new User(1L,"user1","password", "email@email.com");
        given(userRepository.findOne(1L)).willReturn(expected);

        assertTrue("The user is returned as is from the repository", expected == userService.findOne(1L));
    }

    @Test
    public void when_SaveNewUser_The_User_Is_Saved() throws Exception {
        User userToBeSaved = new User(null,"user1","password", "email@email.com");

        // When the mock repo is called, the User ID is set
        given(userRepository.save(userToBeSaved)).willAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                userToBeSaved.setId(1L);
                return userToBeSaved;
            }
        });

        User savedUser = userService.save(userToBeSaved);

        verify(userRepository).save(userToBeSaved);
        assertTrue("The returned saved user id is not null ", savedUser == userToBeSaved);
    }

}

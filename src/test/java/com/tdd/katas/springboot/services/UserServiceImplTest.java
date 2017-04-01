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
import static org.mockito.Mockito.*;


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
    public void when_SaveOrUpdateTheUser_The_User_Is_Updated_Or_Created() throws Exception {
        User userToBeCreatedOrUpdated = new User(1l,"user1","password", "email@email.com");

        given(userRepository.save(userToBeCreatedOrUpdated)).willReturn(userToBeCreatedOrUpdated);

        User createdOrUpdatedUser = userService.save(userToBeCreatedOrUpdated);

        verify(userRepository).save(userToBeCreatedOrUpdated);
        assertTrue("The user is created or updated and returned as is from the repository", createdOrUpdatedUser == userToBeCreatedOrUpdated);
    }

    @Test
    public void when_RemoveOneUser_The_User_Is_Removed() throws Exception {
        User userToBeDelete = new User(1L,"user1","password", "email@email.com");

        userService.delete(userToBeDelete);

        verify(userRepository).delete(userToBeDelete);
    }

    @Test
    public void service_Always_Propagates_Exceptions() throws Exception {
        User user = new User(1L,"user1","password", "email@email.com");

        IllegalStateException databaseNotOpenException = new IllegalStateException("Database not open!");

        doThrow(databaseNotOpenException).when(userRepository).findAll();
        assertThrowsIllegalStateException(() -> userRepository.findAll());

        doThrow(databaseNotOpenException).when(userRepository).findOne(1L);
        assertThrowsIllegalStateException(() -> userRepository.findOne(1L));

        doThrow(databaseNotOpenException).when(userRepository).save(user);
        assertThrowsIllegalStateException(() -> userRepository.save(user));

        doThrow(databaseNotOpenException).when(userRepository).delete(user);
        assertThrowsIllegalStateException(() -> userRepository.delete(user));

        assertThrowsIllegalStateException(new Runnable() {
            @Override
            public void run() {
                userRepository.delete(user);
            }
        });
    }


    private void assertThrowsIllegalStateException(Runnable runnable) {
        try {
            runnable.run();
            fail("Should have thrown exception");
        }
        catch (IllegalStateException e) {
        }
    }


}

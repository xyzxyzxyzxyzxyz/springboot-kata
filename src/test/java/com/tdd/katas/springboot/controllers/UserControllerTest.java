package com.tdd.katas.springboot.controllers;

import com.tdd.katas.springboot.model.User;
import com.tdd.katas.springboot.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@WithMockUser
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;

    @Test
    public void testFindAllUsers() throws Exception {

        List<User> expectedUserList = new ArrayList<>();
        expectedUserList.add(new User(1L,"user1","password", "email@email.com"));

        given(this.userService.findAll())
                .willReturn(expectedUserList);

        this.mvc.perform(
                    get("/users/")
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id", is(expectedUserList.get(0).getId().intValue())));
    }

    @Test
    public void testFindOneUser() throws Exception {

        User expectedUser = new User(1L,"user1","password", "email@email.com");

        given(this.userService.findOne(1L))
                .willReturn(expectedUser);

        this.mvc.perform(
                get("/users/1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(expectedUser.getId().intValue())))
                .andExpect(jsonPath("$.login", is(expectedUser.getLogin())))
                .andExpect(jsonPath("$.password", is(expectedUser.getPassword())))
                .andExpect(jsonPath("$.email", is(expectedUser.getEmail()))
        );
    }

    @Test
    public void testCreateUser() throws Exception {

        User createdUser = new User(1L,"user1","password", "email@email.com");

        given(this.userService.save(any(User.class)))
                .willReturn(createdUser);

        this.mvc.perform(
                    post("/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            "{" +
                                    "\"login\": \""+ createdUser.getLogin() + "\"," +
                                    "\"password\": \""+ createdUser.getPassword() + "\"," +
                                    "\"email\": \""+ createdUser.getEmail() + "\"" +
                            "}"
                    )
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, endsWith("/users/1")));

    }

    @Test
    public void testUpdateUser() throws Exception {

        User userToBeUpdated = new User(1L,"user1","password", "email@email.com");

        given(this.userService.save(userToBeUpdated))
                .willReturn(userToBeUpdated);

        this.mvc.perform(
                    put("/users/" + userToBeUpdated.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            "{" +
                                " \"id\": " + userToBeUpdated.getId() + "," +
                                " \"login\": \"" + userToBeUpdated.getLogin() + "\", " +
                                " \"password\": \"" + userToBeUpdated.getPassword() + "\", " +
                                " \"email\": \"" + userToBeUpdated.getEmail() + "\" " +
                            "}"
                    )
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(userToBeUpdated.getId().intValue())))
                .andExpect(jsonPath("$.login", is(userToBeUpdated.getLogin())))
                .andExpect(jsonPath("$.password", is(userToBeUpdated.getPassword())))
                .andExpect(jsonPath("$.email", is(userToBeUpdated.getEmail())));

    }

}

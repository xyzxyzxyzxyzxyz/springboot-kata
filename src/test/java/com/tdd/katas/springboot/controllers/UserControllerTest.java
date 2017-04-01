package com.tdd.katas.springboot.controllers;

import com.tdd.katas.springboot.model.User;
import com.tdd.katas.springboot.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.is;
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
                .andExpect(jsonPath("$[0].id", is(expectedUserList.get(0).getId().intValue())));

    }
}

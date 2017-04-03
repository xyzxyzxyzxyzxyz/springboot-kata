package com.tdd.katas.springboot.controllers;

import com.tdd.katas.springboot.model.User;
import com.tdd.katas.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User findOne(@PathVariable long userId){
        return userService.findOne(userId);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user){
        User savedUser = userService.save(user);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/users/" + savedUser.getId());

        ResponseEntity<?> response = new ResponseEntity<Object>(headers, HttpStatus.CREATED);
        return response;
    }

}

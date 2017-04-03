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
    public ResponseEntity<User> findOne(@PathVariable long userId){

        ResponseEntity<User> response;

        User user =  userService.findOne(userId);

        if(user != null) {
            response = new ResponseEntity<>(user, HttpStatus.OK);
        }else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user){
        if (user.getId()!=null) {
            throw new IllegalArgumentException("This endpoint is for creating new users only");
        }

        User savedUser = userService.save(user);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/users/" + savedUser.getId());

        ResponseEntity<?> response = new ResponseEntity<Object>(headers, HttpStatus.CREATED);
        return response;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable long userId, @RequestBody User userToBeUpdated){

        if (userToBeUpdated.getId()!=null && userId != userToBeUpdated.getId()) {
            throw new IllegalArgumentException("The id used in the url must match with the entity id");
        }

        //overriding the id when it is null
        userToBeUpdated.setId(userId);

        User updatedUser = userService.save(userToBeUpdated);

        ResponseEntity<User> response = new ResponseEntity<>(updatedUser, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable long userId){

        userService.delete(userId);

        ResponseEntity<?> response = new ResponseEntity<>( HttpStatus.NO_CONTENT);
        return response;
    }

}

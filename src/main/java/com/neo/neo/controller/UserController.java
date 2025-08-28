package com.neo.neo.controller;

import com.neo.neo.DTO.LoginRequest;
import com.neo.neo.entity.User;
import com.neo.neo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {


    @Autowired
    UserService userService;



    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        return userService.createUser(user);
    }


    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody LoginRequest loginRequest){
        return userService.loginUser(loginRequest.name(), loginRequest.password());
    }



}

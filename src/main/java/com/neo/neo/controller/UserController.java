package com.neo.neo.controller;

import com.neo.neo.DTO.request.LoginRequest;
import com.neo.neo.DTO.request.UpdateUserRequest;
import com.neo.neo.entity.User;
import com.neo.neo.service.UserService;
import com.neo.neo.specifications.UserSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @DeleteMapping("/auth/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }


    @GetMapping("/auth/users")
    public Page<User> getUsers(@RequestParam(required = false) String email, @RequestParam(required = false) String name, @RequestParam(required = false) String cpf, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Specification<User> spec = UserSpecifications.nameContains(name)
                .and(UserSpecifications.cpfContains(cpf))
                .and(UserSpecifications.emailContains(email));

        return userService.getAllUser(page, size, spec);
    }


    @PutMapping("/auth/update/user/{id}")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest){
        return userService.updateUser(id, updateUserRequest);
    }



}

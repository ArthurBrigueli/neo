package com.neo.neo.service;


import com.neo.neo.DTO.LoginRequest;
import com.neo.neo.DTO.LoginResponse;
import com.neo.neo.DTO.UserResponse;
import com.neo.neo.configSecurity.TokenService;
import com.neo.neo.entity.User;
import com.neo.neo.repository.UserRepository;
import jakarta.validation.Valid;
import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    private final PasswordEncoder encoder;
    TokenService tokenService;

    public UserService(UserRepository userRepository, PasswordEncoder encoder, TokenService tokenService){
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.tokenService = tokenService;
    }


    public ResponseEntity<String> createUser( User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().body("Usuario criado com sucesso");
    }


    public ResponseEntity loginUser( String name, String password){

        User user = this.userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User Not found"));


        if (encoder.matches(password, user.getPassword())) {
            String token = tokenService.generateToken(user);

            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getCpf(),
                    user.getDateOfBirth(),
                    user.getAge()
            );

            LoginResponse loginResponse = new LoginResponse(userResponse, token);

            return ResponseEntity.ok(loginResponse);
        }

        return ResponseEntity
                .badRequest()
                .body("Credenciais incorreta");
    }


}

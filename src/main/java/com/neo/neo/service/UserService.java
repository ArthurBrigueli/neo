package com.neo.neo.service;


import com.neo.neo.DTO.request.UpdateUserRequest;
import com.neo.neo.DTO.response.LoginResponse;
import com.neo.neo.DTO.response.UserResponse;
import com.neo.neo.configSecurity.TokenService;
import com.neo.neo.entity.User;
import com.neo.neo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

    public ResponseEntity updateUser(Long id, UpdateUserRequest updateUserRequest){
        return userRepository.findById(id)
                .map(user -> {
                    if(updateUserRequest.email() != null){
                        userRepository.findByEmail(updateUserRequest.email())
                                .filter(u -> !u.getId().equals(id))
                                .ifPresent(u -> { throw new RuntimeException("Email já cadastrado"); });
                        user.setEmail(updateUserRequest.email());
                    }

                    if(updateUserRequest.cpf() != null) {
                        userRepository.findByCpf(updateUserRequest.cpf())
                                .filter(u -> !u.getId().equals(id))
                                .ifPresent(u -> { throw new RuntimeException("CPF já cadastrado"); });
                        user.setCpf(updateUserRequest.cpf());
                    }

                    if(updateUserRequest.name() != null){
                        user.setName(updateUserRequest.name());
                    }

                    if(updateUserRequest.dateOfBirth() != null){
                        user.setDateOfBirth(updateUserRequest.dateOfBirth());
                    }

                    if(updateUserRequest.password() != null && !updateUserRequest.password().isEmpty()){
                        user.setPassword(encoder.encode(updateUserRequest.password()));
                    }

                    userRepository.save(user);
                    return ResponseEntity.ok().body("Usuario Atualizado com sucesso");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    public ResponseEntity deleteUser(Long id){
        Optional<User> optinalUser = userRepository.findById(id);
        if(optinalUser.isPresent()){
            userRepository.deleteById(id);
            return ResponseEntity.ok().body("Usuario deletado com sucesso");
        }
        return ResponseEntity.badRequest().body("Usuario nao encontrado");
    }


    public Page<User> getAllUser(int page, int size, Specification<User> specification){
        PageRequest pageRequest = PageRequest.of(page, size);
        return userRepository.findAll(specification, pageRequest);
    }


    public ResponseEntity<String> createUser( User user){

        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Email já cadastrado");
        }

        if(userRepository.findByCpf(user.getCpf()).isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body("Cpf já cadastrado");
        }



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

package com.neo.neo.service;


import com.neo.neo.DTO.request.CreateUserRequest;
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


    public Page<UserResponse> getAllUser(int page, int size, Specification<User> specification){
        PageRequest pageRequest = PageRequest.of(page, size);
        return userRepository.findAll(specification, pageRequest)
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCpf(),
                        user.getDateOfBirth(),
                        user.getAge()
                ));
    }


    public void createUser(CreateUserRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email ja cadastrado");
        }

        if (userRepository.findByCpf(request.cpf()).isPresent()) {
            throw  new IllegalArgumentException("Cpf ja cadastrado");
        }

        User user = new User();
        user.setName(request.name());
        user.setCpf(request.cpf());
        user.setEmail(request.email());
        user.setDateOfBirth(request.dateOfBirth());
        user.setPassword(encoder.encode(request.password()));

        userRepository.save(user);

    }


    public LoginResponse loginUser( String name, String password){

        User user = this.userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User Not found"));


        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciais incorretas");
        }

        String token = tokenService.generateToken(user);

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getDateOfBirth(),
                user.getAge()
        );

        return new LoginResponse(userResponse, token);

    }


}

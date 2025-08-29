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

    public void updateUser(Long id, UpdateUserRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));

        if(request.email() != null){
            userRepository.findByEmail(request.email())
                    .filter(u -> !u.getId().equals(id))
                    .ifPresent(u -> { throw new IllegalArgumentException("Email ja cadastrado"); });
            user.setEmail(request.email());
        }

        if(request.cpf() != null){
            userRepository.findByCpf(request.cpf())
                    .filter(u -> !u.getId().equals(id))
                    .ifPresent(u -> { throw new IllegalArgumentException("CPF ja cadastrado"); });
            user.setCpf(request.cpf());
        }

        if(request.name() != null) user.setName(request.name());

        if(request.dateOfBirth() != null) user.setDateOfBirth(request.dateOfBirth());

        if(request.password() != null && !request.password().isEmpty()){
            user.setPassword(encoder.encode(request.password()));
        }

        userRepository.save(user);
    }



    public void deleteUser(Long id){
        Optional<User> optinalUser = userRepository.findById(id);
        if(optinalUser.isPresent()){
            userRepository.deleteById(id);
        }else{
            throw new IllegalArgumentException("Usuario nao encontrado");
        }

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

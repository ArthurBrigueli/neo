package com.neo.neo.service;


import com.neo.neo.DTO.request.CreateUserRequest;
import com.neo.neo.DTO.request.LoginRequest;
import com.neo.neo.entity.User;
import com.neo.neo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void registerUserSucess(){

        CreateUserRequest user = new CreateUserRequest(
                "arthur",
                "123.321.432-89",
                "arthur@gmail.com",
                "18/02/2004",
                "arthur"
        );

        when(userRepository.findByEmail(user.email())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.cpf())).thenReturn(Optional.empty());
        when(encoder.encode(user.password())).thenReturn("senhaCodificada");

        userService.createUser(user);

        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    public void registerUserWithEmailExists(){
        CreateUserRequest user = new CreateUserRequest(
                "arthur",
                "123.321.432-89",
                "arthur@gmail.com",
                "18/02/2004",
                "arthur"
        );

        when(userRepository.findByEmail(user.email())).thenReturn(Optional.of(new User()));
        assertThrows(IllegalArgumentException.class, ()->userService.createUser(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void registerUserWithCpfExists(){
        CreateUserRequest user = new CreateUserRequest(
                "arthur",
                "123.321.432-89",
                "arthur@gmail.com",
                "18/02/2004",
                "arthur"
        );

        when(userRepository.findByCpf(user.cpf())).thenReturn(Optional.of(new User()));
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any(User.class));

    }
    







}

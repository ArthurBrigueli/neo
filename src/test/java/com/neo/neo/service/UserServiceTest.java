package com.neo.neo.service;


import com.neo.neo.DTO.request.CreateUserRequest;
import com.neo.neo.DTO.request.LoginRequest;
import com.neo.neo.DTO.request.UpdateUserRequest;
import com.neo.neo.DTO.response.LoginResponse;
import com.neo.neo.configSecurity.TokenService;
import com.neo.neo.entity.User;
import com.neo.neo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private TokenService tokenService;

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


    @Test
    public void loginSucess(){

        User user = new User();

        user.setId(1L);
        user.setName("arthur");
        user.setCpf("123.123.123-32");
        user.setEmail("arthur@gmail.com");
        user.setPassword("arthur");
        user.setDateOfBirth("18/02/2004");

        when(userRepository.findByName("arthur")).thenReturn(Optional.of(user));
        when(encoder.matches("arthur", user.getPassword())).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("token123");

        LoginResponse response = userService.loginUser("arthur", "arthur");

        assertNotNull(response);
        assertEquals("arthur", response.user().name());
        assertEquals("token123", response.token());
        verify(userRepository, times(1)).findByName("arthur");

    }

    @Test
    public void loginErrorUserNotFound(){

        when(userRepository.findByName("arthur")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, ()->{
            userService.loginUser("arthur", "arthur");
        });

        verify(userRepository, times(1)).findByName("arthur");

    }

    @Test
    public void loginErrorPasswordIncorret(){
        User user = new User();

        user.setId(1L);
        user.setName("arthur");
        user.setCpf("123.123.123-32");
        user.setEmail("arthur@gmail.com");
        user.setPassword("arthur");
        user.setDateOfBirth("18/02/2004");

        when(userRepository.findByName("arthur")).thenReturn(Optional.of(user));

        when(encoder.matches("arthur", user.getPassword())).thenReturn(false);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            userService.loginUser("arthur", "arthur");
        });

        assertEquals("Credenciais incorretas", runtimeException.getMessage());

        verify(userRepository, times(1)).findByName("arthur");

    }


    @Test
    public void deleteUserSuccess(){
        User user = new User();
        user.setId(1L);
        user.setName("arthur");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());

    }

    @Test
    public void deleteUserNotFound(){

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("Usuario nao encontrado", exception.getMessage());

        verify(userRepository, never()).deleteById(anyLong());

    }


    @Test
    public void updateUserSuccess() {
        User user = new User();
        user.setId(1L);
        user.setName("arthur");
        user.setCpf("123.123.123-32");
        user.setEmail("arthur@gmail.com");
        user.setPassword("arthur");
        user.setDateOfBirth("18/02/2004");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("novo@email.com")).thenReturn(Optional.empty());
        when(userRepository.findByCpf("987.654.321-00")).thenReturn(Optional.empty());
        when(encoder.encode("novaSenha")).thenReturn("senhaCodificada");

        UpdateUserRequest request = new UpdateUserRequest(
                "Novo Nome",
                "987.654.321-00",
                "novo@email.com",
                "01/01/2000",
                "novaSenha"
        );

        userService.updateUser(user.getId(), request);

        verify(userRepository, times(1)).save(argThat(u ->
                u.getName().equals("Novo Nome") &&
                        u.getCpf().equals("987.654.321-00") &&
                        u.getEmail().equals("novo@email.com") &&
                        u.getDateOfBirth().equals("01/01/2000") &&
                        u.getPassword().equals("senhaCodificada")
        ));
    }

    @Test
    public void UpdateUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateUserRequest request = new UpdateUserRequest(
                "Nome",
                "123.456.789-00",
                "email@teste.com",
                "01/01/2000",
                "senha"
        );

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(1L, request);
        });
    }


    @Test
    public void updateUserEmailAlreadyExists() {
        User existingUser = new User();
        existingUser.setId(2L); // outro usuário
        existingUser.setEmail("email@teste.com");

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("email@teste.com")).thenReturn(Optional.of(existingUser));

        UpdateUserRequest request = new UpdateUserRequest(
                null,
                null,
                "email@teste.com",
                null,
                null
        );

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(1L, request);
        });
    }



    @Test
    public void updateUserCpfAlreadyExists() {
        User existingUser = new User();
        existingUser.setId(2L); // outro usuário
        existingUser.setCpf("123.456.789-00");

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(existingUser));

        UpdateUserRequest request = new UpdateUserRequest(
                null,
                "123.456.789-00",
                null,
                null,
                null
        );

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(1L, request);
        });
    }












}

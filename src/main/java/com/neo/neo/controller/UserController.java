package com.neo.neo.controller;

import com.neo.neo.DTO.request.CreateUserRequest;
import com.neo.neo.DTO.request.LoginRequest;
import com.neo.neo.DTO.request.UpdateUserRequest;
import com.neo.neo.DTO.response.LoginResponse;
import com.neo.neo.DTO.response.UserResponse;
import com.neo.neo.entity.User;
import com.neo.neo.service.UserService;
import com.neo.neo.specifications.UserSpecifications;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
public class UserController {


    @Autowired
    UserService userService;



    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário")
    public ResponseEntity<String> registerUser(@RequestBody CreateUserRequest createUserRequest){
        try{
            userService.createUser(createUserRequest);
            return ResponseEntity.ok("Usuario criado com sucesso");
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "Login para receber o token jwt para acesso a endpoints privadas")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        try{
            LoginResponse response = userService.loginUser(loginRequest.name(), loginRequest.password());
            return ResponseEntity.ok(response);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @Operation(summary = "Deletar usuario apartir do ID (Bearer necessario)")
    @DeleteMapping("/auth/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try{
            userService.deleteUser(id);
            return ResponseEntity.ok("Usuario deletado com sucesso");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @Operation(
            summary = "Buscar usuarios com paginação e filtros de nome, email e cpf (Bearer necessario)"
    )
    @GetMapping("/auth/users")
    public Page<UserResponse> getUsers(@RequestParam(required = false) String email, @RequestParam(required = false) String name, @RequestParam(required = false) String cpf, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Specification<User> spec = UserSpecifications.nameContains(name)
                .and(UserSpecifications.cpfContains(cpf))
                .and(UserSpecifications.emailContains(email));

        return userService.getAllUser(page, size, spec);
    }


    @Operation(summary = "Editar informaçoes do usuario apartir do ID (Bearer necessario)")
    @PutMapping("/auth/update/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest){
        try{
            userService.updateUser(id, updateUserRequest);
            return ResponseEntity.ok("Usuario atualizado com sucesso");
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}

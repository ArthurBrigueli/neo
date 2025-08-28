package com.neo.neo.DTO;

public record UserResponse(
        Long id,
        String name,
        String email,
        String cpf,
        String dateOfBirth,
        int age
) {
}

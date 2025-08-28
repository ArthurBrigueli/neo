package com.neo.neo.DTO.response;

public record UserResponse(
        Long id,
        String name,
        String email,
        String cpf,
        String dateOfBirth,
        int age
) {
}

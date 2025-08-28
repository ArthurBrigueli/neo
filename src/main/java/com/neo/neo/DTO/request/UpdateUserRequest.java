package com.neo.neo.DTO.request;

public record UpdateUserRequest(
        String name,
        String cpf,
        String email,
        String dateOfBirth,
        String password
) {
}

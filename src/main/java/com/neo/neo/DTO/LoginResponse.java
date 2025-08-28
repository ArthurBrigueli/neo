package com.neo.neo.DTO;

public record LoginResponse(
        UserResponse user,
        String token
) {
}

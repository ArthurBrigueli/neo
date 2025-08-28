package com.neo.neo.DTO.response;

public record LoginResponse(
        UserResponse user,
        String token
) {
}

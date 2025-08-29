package com.neo.neo.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(

        @Schema(description = "Nome de acesso do usuario", example = "Admin")
        String name,

        @Schema(description = "senha de acesso do usuario", example = "adminsenha1")
        String password
) {
}

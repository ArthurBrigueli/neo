package com.neo.neo.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateUserRequest(

        @Schema(description = "Nome completo do usuário", example = "Admin")
        String name,

        @Schema(description = "Cpf do usuário", example = "123.432.543-55")
        String cpf,

        @Schema(description = "Email do usuário", example = "exampleAdmin@gmail.com")
        String email,

        @Schema(description = "Data de nascimento do usuário", example = "18/02/2004")
        String dateOfBirth,

        @Schema(description = "Senha do usuário", example = "adminsenha1")
        String password
) {
}

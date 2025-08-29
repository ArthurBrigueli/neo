package com.neo.neo.DTO.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateUserRequest(

        @Schema(description = "Novo nome do usuário", nullable = true)
        String name,

        @Schema(description = "Novo Cpf do usuário", nullable = true)
        String cpf,

        @Schema(description = "Novo E-mail do usuário", example = "example@gmail.com", nullable = true)
        String email,

        @Schema(description = "Nova data de nascimento do usuario", nullable = true)
        String dateOfBirth,

        @Schema(description = "Nova senha do usuario", nullable = true)
        String password
) {
}

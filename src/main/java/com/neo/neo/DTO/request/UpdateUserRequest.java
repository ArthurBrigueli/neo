package com.neo.neo.DTO.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateUserRequest(

        @Schema(description = "Novo nome do usuário", nullable = true, example = "novoNome")
        String name,

        @Schema(description = "Novo Cpf do usuário", nullable = true, example = "123.321.444-44")
        String cpf,

        @Schema(description = "Novo E-mail do usuário", example = "example@gmail.com", nullable = true)
        String email,

        @Schema(description = "Nova data de nascimento do usuario", nullable = true, example = "18/02/2004")
        String dateOfBirth,

        @Schema(description = "Nova senha do usuario", nullable = true, example = "senhanova123")
        String password
) {
}

package org.lib.securityservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "JWT запрос")
public class JwtRequestDTO {

    @Schema(description = "Почта", example = "user@gmail.com")
    private String email;

    @Schema(description = "Пароль", example = "password")
    private String password;
}

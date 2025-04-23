package org.lib.securityservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO Ответ при валидации токена")
public class TokenValidationResponse {

    @Schema(description = "почта", example = "mail@gym.com")
    private String email;

    @Schema(description = "роль", example = "ROLE_ADMIN")
    private String role;
}

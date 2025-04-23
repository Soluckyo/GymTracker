package org.lib.usermanagementservice.dto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO Ответ при валидации токена")
public class JwtValidationResponse {
    @Schema(description = "почта", example = "mail@gym.com")
    private String email;

    @Schema(description = "роль", example = "ROLE_ADMIN")
    private String role;
}

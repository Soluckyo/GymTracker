package org.lib.usermanagementservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Schema(description = "DTO Ответ при валидации токена")
public class TokenValidationResponse {

    @Schema(description = "Id пользователя", example = "mail@gym.com")
    private UUID userId;

    @Schema(description = "почта", example = "mail@gym.com")
    private String email;

    @Schema(description = "роль", example = "ROLE_ADMIN")
    private String role;
}

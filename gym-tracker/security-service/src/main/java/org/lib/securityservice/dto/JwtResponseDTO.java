package org.lib.securityservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "JWT ответ")
public class JwtResponseDTO {

    @Schema(description = "Токен доступа")
    private String token;

    @Schema(description = "Токен обновления")
    private String refreshToken;
}

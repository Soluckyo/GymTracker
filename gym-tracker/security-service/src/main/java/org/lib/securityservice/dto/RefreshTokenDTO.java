package org.lib.securityservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Токен обновления")
public class RefreshTokenDTO {

    @Schema(description = "Токен обновления")
    private String refreshToken;
}

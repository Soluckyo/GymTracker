package org.lib.securityservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для регистрации")
public class RegisterRequest {
    @Email
    @NotBlank(message = "Email не может быть пустым")
    @Schema(description = "почта", example = "mail@gym.com")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "пароль", example = "password")
    private String password;

    @Schema(description = "роль", example = "ROLE_ADMIN")
    private String role;

    @Schema(description = "Id из user-management-service", example = "12")
    private UUID externalUserId;
}

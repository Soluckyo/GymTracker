package org.lib.usermanagementservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на регистрацию AppUser из security-service")
public class RegisterAppUserRequest {

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
    private Long externalUserId;
}

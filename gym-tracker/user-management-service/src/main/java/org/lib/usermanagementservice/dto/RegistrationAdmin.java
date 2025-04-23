package org.lib.usermanagementservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для регистрации администратора")
public class RegistrationAdmin {

    @Schema(description = "Id администратора (необязательно указывать)", example = "12")
    private Long id;

    @Schema(description = "Имя администратора", example = "John")
    private String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email
    @Schema(description = "почта", example = "mail@gym.com")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "пароль", example = "password")
    private String password;
}

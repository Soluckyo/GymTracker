package org.lib.usermanagementservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для регистрации администратора")
public class RegistrationUser {

    @Schema(description = "Id пользователя (необязательно указывать)", example = "12")
    private UUID userId;

    @Schema(description = "Имя", example = "Боб")
    private String firstName;

    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;

    @Column(unique = true)
    @NotBlank
    @Email(message = "Пожалуйста введите корректный email")
    @Schema(description = "почта", example = "mail@gym.com")
    private String email;

    @NotBlank
    @Schema(description = "пароль", example = "пароль12")
    private String password;

}

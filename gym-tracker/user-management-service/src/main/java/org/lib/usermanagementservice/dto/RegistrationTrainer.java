package org.lib.usermanagementservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "DTO для регистрации администратора")
public class RegistrationTrainer {

    @Schema(description = "Id тренера (необязательно указывать)", example = "12")
    private UUID trainerId;

    @Schema(description = "Имя администратора", example = "Боб")
    private String name;

    @Email
    @NotBlank(message = "Email не может быть пустым")
    @Schema(description = "почта", example = "mail@gym.com")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "пароль", example = "пароль12")
    private String password;

    @Schema(description = "Специализация тренера", example = "Пауэрлифтинг")
    private String specialization;

    @Schema(description = "Опыт работы тренера в годах", example = "2")
    private String workExperience;
}

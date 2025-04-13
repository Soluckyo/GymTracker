package org.lib.usermanagementservice.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationTrainer {
    private String name;

    @Email(message = "Пожалуйста введите корректный email")
    private String email;

    private String password;

    private String specialization;

    private String workExperience;
}

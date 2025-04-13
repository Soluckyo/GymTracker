package org.lib.usermanagementservice.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationAdmin {

    private String name;

    @Email(message = "Пожалуйста введите корректный email")
    private String email;

    private String password;
}

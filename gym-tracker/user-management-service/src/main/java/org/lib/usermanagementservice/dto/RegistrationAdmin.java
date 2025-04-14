package org.lib.usermanagementservice.dto;

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
public class RegistrationAdmin {

    private Long id;

    private String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}

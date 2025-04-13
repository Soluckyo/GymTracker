package org.lib.usermanagementservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUser {
    private String firstName;
    private String lastName;

    @Column(unique = true)
    @Email(message = "Пожалуйста введите корректный email")
    private String email;
    private String password;

}

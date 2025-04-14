package org.lib.usermanagementservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUser {

    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    @NotBlank
    @Email(message = "Пожалуйста введите корректный email")
    private String email;

    @NotBlank
    private String password;

}

package org.lib.usermanagementservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.lib.usermanagementservice.client.AuthClient;
import org.lib.usermanagementservice.dto.RegisterAppUserRequest;
import org.lib.usermanagementservice.dto.RegistrationUser;
import org.lib.usermanagementservice.entity.User;
import org.lib.usermanagementservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final AuthClient authClient;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthClient authClient, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authClient = authClient;
        this.passwordEncoder = passwordEncoder;
    }

    //TODO: добавить encoder
    public User registerUser(RegistrationUser registrationUser) {
        if (registrationUser == null) {
            throw new IllegalArgumentException("Данные полей для регистрации пользователя не заполнены");
        }
        User user = User.builder()
                .email(registrationUser.getEmail())
                .firstName(registrationUser.getFirstName())
                .lastName(registrationUser.getLastName())
                .password(passwordEncoder.encode(registrationUser.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        RegisterAppUserRequest authUser = new RegisterAppUserRequest();
        authUser.setEmail(registrationUser.getEmail());
        authUser.setPassword(passwordEncoder.encode(registrationUser.getPassword()));
        authUser.setRole("USER");
        authUser.setExternalUserId(registrationUser.getId());

        authClient.registerAppUser(authUser);

        return savedUser;
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(() -> new EntityNotFoundException("Пользователь с данным Email: " + email + " не найден"));
    }

    public void deleteUserByEmail(String email) {
        if(userRepository.getUserByEmail(email).isEmpty()) {
            throw new EntityNotFoundException("Пользователя с таким Email: " + email + " не существует");
        }
        userRepository.deleteByEmail(email);
    }

}

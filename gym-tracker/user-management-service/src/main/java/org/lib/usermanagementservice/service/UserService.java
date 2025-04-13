package org.lib.usermanagementservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.lib.usermanagementservice.dto.RegistrationUser;
import org.lib.usermanagementservice.entity.User;
import org.lib.usermanagementservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO: добавить encoder
    public void registerUser(RegistrationUser registrationUser) {
        if (registrationUser == null) {
            throw new IllegalArgumentException("Данные полей для регистрации пользователя не заполнены");
        }
        User user = User.builder()
                .email(registrationUser.getEmail())
                .firstName(registrationUser.getFirstName())
                .lastName(registrationUser.getLastName())
                .password(registrationUser.getPassword())
                .build();
        userRepository.save(user);
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

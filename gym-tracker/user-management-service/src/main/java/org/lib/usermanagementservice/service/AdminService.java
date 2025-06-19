package org.lib.usermanagementservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.lib.usermanagementservice.client.AuthClient;
import org.lib.usermanagementservice.dto.RegisterAppUserRequest;
import org.lib.usermanagementservice.dto.RegistrationAdmin;
import org.lib.usermanagementservice.dto.RegistrationTrainer;
import org.lib.usermanagementservice.dto.RegistrationUser;
import org.lib.usermanagementservice.entity.Admin;
import org.lib.usermanagementservice.entity.Trainer;
import org.lib.usermanagementservice.entity.User;
import org.lib.usermanagementservice.exception.EmailAlreadyExistsException;
import org.lib.usermanagementservice.repository.AdminRepository;
import org.lib.usermanagementservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AdminService implements IAdminService {

    private final AdminRepository adminRepository;
    private final ITrainerService trainerService;
    private final IUserService userService;
    private final UserRepository userRepository;
    private final AuthClient authClient;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, ITrainerService trainerService, IUserService userService, UserRepository userRepository, AuthClient authClient, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.trainerService = trainerService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.authClient = authClient;
        this.passwordEncoder = passwordEncoder;
    }
    //TODO: изменить логику возвращения(не возвращать пароль)
    public Admin registerAdmin(RegistrationAdmin registrationAdmin) {
        if(registrationAdmin == null) {
            throw new IllegalArgumentException("Данные полей для регистрации администратора не заполнены");
        }


        CompletableFuture<Boolean> emailCheckFuture = CompletableFuture.supplyAsync(
                () -> adminRepository.existsByEmail(registrationAdmin.getEmail())
        );

        String encodedPass = passwordEncoder.encode(registrationAdmin.getPassword());

        if (emailCheckFuture.join()) {
            throw new EmailAlreadyExistsException("Email уже используется");
        }

        Admin admin = Admin.builder()
                .name(registrationAdmin.getName())
                .email(registrationAdmin.getEmail())
                .password(encodedPass)
                .build();

        Admin savedAdmin = adminRepository.save(admin);

        RegisterAppUserRequest authUser = new RegisterAppUserRequest();
        authUser.setEmail(admin.getEmail());
        authUser.setPassword(encodedPass);
        authUser.setRole("ADMIN");
        authUser.setExternalUserId(savedAdmin.getAdminId());

        try{
            authClient.registerAppUser(authUser);
        }catch(Exception e){
            log.error("Регистрация пользователя в сервисе безопасности не удалась," +
                    " но пользователь создан локально. Ошибка: {}", e.getMessage());
        }
        return savedAdmin;
    }

    public void registerTrainer(RegistrationTrainer registrationTrainer) {
        trainerService.registerTrainer(registrationTrainer);
    }

    public void registerUser(RegistrationUser registrationUser) {
        userService.registerUser(registrationUser);
    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUser(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Пользователь с таким email не найден"));
    }

    public Page<Trainer> getTrainers(Pageable pageable) {
        return trainerService.findAll(pageable);
    }
}

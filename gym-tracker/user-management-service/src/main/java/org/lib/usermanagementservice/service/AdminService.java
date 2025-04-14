package org.lib.usermanagementservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.lib.usermanagementservice.client.AuthClient;
import org.lib.usermanagementservice.dto.RegisterAppUserRequest;
import org.lib.usermanagementservice.dto.RegistrationAdmin;
import org.lib.usermanagementservice.dto.RegistrationTrainer;
import org.lib.usermanagementservice.dto.RegistrationUser;
import org.lib.usermanagementservice.entity.Admin;
import org.lib.usermanagementservice.entity.User;
import org.lib.usermanagementservice.repository.AdminRepository;
import org.lib.usermanagementservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements IAdminService {

    private final AdminRepository adminRepository;
    private final ITrainerService trainerService;
    private final IUserService userService;
    private final UserRepository userRepository;
    private final AuthClient authClient;

    public AdminService(AdminRepository adminRepository, ITrainerService trainerService, IUserService userService, UserRepository userRepository, AuthClient authClient) {
        this.adminRepository = adminRepository;
        this.trainerService = trainerService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.authClient = authClient;
    }

    public Admin registerAdmin(RegistrationAdmin registrationAdmin) {
        if(registrationAdmin == null) {
            throw new IllegalArgumentException("Данные полей для регистрации администратора не заполнены");
        }
        Admin admin = Admin.builder()
                .name(registrationAdmin.getName())
                .email(registrationAdmin.getEmail())
                .password(registrationAdmin.getPassword())
                .build();

        Admin savedAdmin = adminRepository.save(admin);

        RegisterAppUserRequest authUser = new RegisterAppUserRequest();
        authUser.setEmail(registrationAdmin.getEmail());
        authUser.setPassword(registrationAdmin.getPassword());
        authUser.setRole("ADMIN");
        authUser.setExternalUserId(registrationAdmin.getId());

        authClient.registerAppUser(authUser);

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
}

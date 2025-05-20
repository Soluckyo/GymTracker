package org.lib.usermanagementservice.service;

import org.lib.usermanagementservice.client.AuthClient;
import org.lib.usermanagementservice.dto.RegisterAppUserRequest;
import org.lib.usermanagementservice.dto.RegistrationTrainer;
import org.lib.usermanagementservice.entity.Trainer;
import org.lib.usermanagementservice.exception.EmailAlreadyExistsException;
import org.lib.usermanagementservice.repository.TrainerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TrainerService implements ITrainerService {
    private final TrainerRepository trainerRepository;
    private final AuthClient authClient;
    private final PasswordEncoder passwordEncoder;

    public TrainerService(TrainerRepository trainerRepository, AuthClient authClient, PasswordEncoder passwordEncoder) {
        this.trainerRepository = trainerRepository;
        this.authClient = authClient;
        this.passwordEncoder = passwordEncoder;
    }

    public Trainer registerTrainer(RegistrationTrainer registrationTrainer) {
        if(registrationTrainer == null) {
            throw new IllegalArgumentException("Данные полей для регистрации тренера не заполнены");
        }
        if(trainerRepository.existsByEmail(registrationTrainer.getEmail())){
            throw new EmailAlreadyExistsException("Такой Email уже используется");
        }
        Trainer trainer = Trainer.builder()
                .trainerId(registrationTrainer.getTrainerId())
                .name(registrationTrainer.getName())
                .email(registrationTrainer.getEmail())
                .password(passwordEncoder.encode(registrationTrainer.getPassword()))
                .specialization(registrationTrainer.getSpecialization())
                .workExperience(registrationTrainer.getWorkExperience())
                .build();
        Trainer savedTrainer = trainerRepository.save(trainer);

        RegisterAppUserRequest authUser = new RegisterAppUserRequest();
        authUser.setEmail(registrationTrainer.getEmail());
        authUser.setPassword(passwordEncoder.encode(registrationTrainer.getPassword()));
        authUser.setRole("TRAINER");
        authUser.setExternalUserId(registrationTrainer.getTrainerId());

        authClient.registerAppUser(authUser);

        return savedTrainer;
    }
}

package org.lib.usermanagementservice.service;

import org.lib.usermanagementservice.dto.RegisterAppUserRequest;
import org.lib.usermanagementservice.dto.RegistrationTrainer;
import org.lib.usermanagementservice.entity.Trainer;
import org.lib.usermanagementservice.repository.TrainerRepository;
import org.springframework.stereotype.Service;

@Service
public class TrainerService implements ITrainerService {
    private final TrainerRepository trainerRepository;
    private final AuthClient authClient;

    public TrainerService(TrainerRepository trainerRepository, AuthClient authClient) {
        this.trainerRepository = trainerRepository;
        this.authClient = authClient;
    }

    public Trainer registerTrainer(RegistrationTrainer registrationTrainer) {
        if(registrationTrainer == null) {
            throw new IllegalArgumentException("Данные полей для регистрации тренера не заполнены");
        }
        Trainer trainer = Trainer.builder()
                .id(registrationTrainer.getId())
                .name(registrationTrainer.getName())
                .email(registrationTrainer.getEmail())
                .password(registrationTrainer.getPassword())
                .specialization(registrationTrainer.getSpecialization())
                .workExperience(registrationTrainer.getWorkExperience())
                .build();
        Trainer savedTrainer = trainerRepository.save(trainer);

        RegisterAppUserRequest authUser = new RegisterAppUserRequest();
        authUser.setEmail(registrationTrainer.getEmail());
        authUser.setPassword(registrationTrainer.getPassword());
        authUser.setRole("TRAINER");
        authUser.setExternalUserId(registrationTrainer.getId());

        authClient.registerAppUser(authUser);

        return savedTrainer;
    }
}

package org.lib.usermanagementservice.service;

import org.lib.usermanagementservice.dto.RegistrationTrainer;
import org.lib.usermanagementservice.entity.Trainer;
import org.lib.usermanagementservice.repository.TrainerRepository;
import org.springframework.stereotype.Service;

@Service
public class TrainerService implements ITrainerService {
    private final TrainerRepository trainerRepository;

    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public void registerTrainer(RegistrationTrainer registrationTrainer) {
        if(registrationTrainer == null) {
            throw new IllegalArgumentException("Данные полей для регистрации тренера не заполнены");
        }
        Trainer trainer = Trainer.builder()
                .name(registrationTrainer.getName())
                .email(registrationTrainer.getEmail())
                .password(registrationTrainer.getPassword())
                .specialization(registrationTrainer.getSpecialization())
                .workExperience(registrationTrainer.getWorkExperience())
                .build();
        trainerRepository.save(trainer);
    }
}

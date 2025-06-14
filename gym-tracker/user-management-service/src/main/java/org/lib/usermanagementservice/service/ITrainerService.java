package org.lib.usermanagementservice.service;


import org.lib.usermanagementservice.dto.RegistrationTrainer;
import org.lib.usermanagementservice.entity.Trainer;
import org.lib.usermanagementservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ITrainerService{
    Trainer registerTrainer(RegistrationTrainer registrationTrainer);
    Trainer findTrainerByUUID(UUID uuid);

    Page<Trainer> findAll(Pageable pageable);
}

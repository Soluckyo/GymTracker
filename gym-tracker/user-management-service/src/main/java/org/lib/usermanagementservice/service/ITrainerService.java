package org.lib.usermanagementservice.service;


import org.lib.usermanagementservice.dto.RegistrationTrainer;
import org.lib.usermanagementservice.entity.Trainer;

import java.util.UUID;

public interface ITrainerService{
    Trainer registerTrainer(RegistrationTrainer registrationTrainer);
    Trainer findTrainerByUUID(UUID uuid);
}

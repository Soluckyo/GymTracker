package org.lib.usermanagementservice.service;


import org.lib.usermanagementservice.dto.RegistrationTrainer;
import org.lib.usermanagementservice.entity.Trainer;

public interface ITrainerService{
    Trainer registerTrainer(RegistrationTrainer registrationTrainer);
}

package org.lib.usermanagementservice.controller;

import org.lib.usermanagementservice.dto.RegistrationTrainer;
import org.lib.usermanagementservice.service.TrainerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainer")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping("/register-trainer")
    public ResponseEntity<String> registerTrainer(@RequestBody RegistrationTrainer registrationTrainer) {
        trainerService.registerTrainer(registrationTrainer);
        return ResponseEntity.ok(registrationTrainer.toString() + "Тренер успешно зарегистрирован");
    }
}

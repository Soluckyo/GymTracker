package org.lib.usermanagementservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.lib.usermanagementservice.dto.RegistrationTrainer;
import org.lib.usermanagementservice.service.TrainerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainer")
@Tag(name="trainer_controller")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Operation(
            summary = "Регистрация тренера",
            description = "Создает нового тренера, принимает DTO RegistrationTrainer"
    )
    @PostMapping("/register-trainer")
    public ResponseEntity<String> registerTrainer(@RequestBody RegistrationTrainer registrationTrainer) {
        trainerService.registerTrainer(registrationTrainer);
        return ResponseEntity.ok(registrationTrainer.toString() + "Тренер успешно зарегистрирован");
    }
}

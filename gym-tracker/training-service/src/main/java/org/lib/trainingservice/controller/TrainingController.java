package org.lib.trainingservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.lib.trainingservice.dto.CreateTrainingDto;
import org.lib.trainingservice.dto.TrainingDTO;
import org.lib.trainingservice.service.ITrainingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/training")
public class TrainingController {

    public ITrainingService trainingService;

    public TrainingController(ITrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping("/create-personal-training")
    public TrainingDTO createTraining(@RequestBody CreateTrainingDto trainingDTO) {
        log.info("Запрос получен: {}", trainingDTO);
        TrainingDTO training = trainingService.createPersonalTraining(trainingDTO);
//        if (training.getTrainerName() == null) {
//            throw new TrainingNotReadyException("Данные о тренере еще не получены");
//        }
        return training;
    }

    @GetMapping("/trainer-name/{trainingId}")
    public String trainerName(@PathVariable UUID trainingId) {
        return trainingService.getTrainerName(trainingId);
    }

    @GetMapping("/all-trainings")
    public Page<TrainingDTO> getAllTrainings(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return trainingService.getAllTrainings(pageable);
    }
}

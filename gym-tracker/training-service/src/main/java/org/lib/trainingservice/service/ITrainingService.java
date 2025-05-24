package org.lib.trainingservice.service;

import org.lib.trainingservice.dto.CreateTrainingDto;
import org.lib.trainingservice.dto.SignUpForTrainingDto;
import org.lib.trainingservice.dto.TrainingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ITrainingService {
    Page<TrainingDTO> getAllTrainings(Pageable pageable);
    Page<TrainingDTO> getAllTrainingsByUserId(UUID userId, Pageable pageable);
    Page<TrainingDTO> getAllGroupTrainings(Pageable pageable);
    Page<TrainingDTO> getAllGroupTrainingsByUserId(UUID userId, Pageable pageable);
    TrainingDTO getTrainingById(UUID trainingId);
    TrainingDTO createGroupTraining(CreateTrainingDto createTrainingDto);
    TrainingDTO createPersonalTraining(CreateTrainingDto createTrainingDto);
    void deleteTraining(UUID trainingId);
    TrainingDTO signUpForTraining(SignUpForTrainingDto signUpForTrainingDto);





}

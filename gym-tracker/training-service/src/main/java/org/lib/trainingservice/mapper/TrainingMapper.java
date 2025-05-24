package org.lib.trainingservice.mapper;

import org.lib.trainingservice.dto.TrainingDTO;
import org.lib.trainingservice.entity.Training;

public class TrainingMapper {
    public static TrainingDTO toTrainingDTO(Training training) {
        TrainingDTO dto = new TrainingDTO();
        dto.setTrainingId(training.getTrainingId());
        dto.setTrainingTitle(training.getTitle());
        dto.setTrainerId(training.getTrainerId());
        dto.setTrainingType(training.getType().toString());
        dto.setDuration(training.getDuration());
        dto.setTrainingStartDate(training.getStartDate());
        dto.setTrainingEndDate(training.getStartDate().plus(training.getDuration()));

        return dto;
    }
}

package org.lib.trainingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TrainingDTO {
    private UUID trainingId;
    private String trainingTitle;
    private UUID trainerId;
    private String trainingType;
    private LocalDateTime trainingStartDate;
    private LocalDateTime trainingEndDate;
    private Duration duration;

    public TrainingDTO() {}

    public TrainingDTO(UUID trainingId, String trainingTitle, UUID trainerId, String trainingType, LocalDateTime trainingStartDate, LocalDateTime trainingEndDate) {
        this.trainingId = trainingId;
        this.trainingTitle = trainingTitle;
        this.trainerId = trainerId;
        this.trainingType = trainingType;
        this.trainingStartDate = trainingStartDate;
        this.trainingEndDate = trainingStartDate.plus(duration);
    }
}

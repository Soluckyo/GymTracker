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
    private String trainerName;
    private String trainingType;
    private LocalDateTime trainingStartTime;
    private LocalDateTime trainingEndTime;
    private Duration duration;
}

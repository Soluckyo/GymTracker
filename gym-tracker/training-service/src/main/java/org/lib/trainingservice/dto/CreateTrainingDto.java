package org.lib.trainingservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.lib.trainingservice.entity.Room;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateTrainingDto {
    private String title;
    private UUID trainerId;
    private LocalDateTime startTime;
    private Duration duration;
    private Room room;
}

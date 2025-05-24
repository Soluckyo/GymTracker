package org.lib.trainingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SignUpForTrainingDto {
    private UUID trainingId;
    private UUID userId;
}

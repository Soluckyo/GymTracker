package org.lib.trainingservice.kafka;

import lombok.RequiredArgsConstructor;
import org.lib.trainingservice.repository.TrainingRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaResponseConsumer {
    private final TrainingRepository trainingRepository;

    @KafkaListener(topics = "trainer-info-response", groupId = "training-group")
    public void consumeTrainerInfoResponse(String message) {
        String[] splitMessage = message.split("&");
        UUID trainingId = UUID.fromString(splitMessage[0]);
        String trainerName = splitMessage[1];

        trainingRepository.findById(trainingId).ifPresent(
                training -> {
                    training.setTrainerName(trainerName);
                    trainingRepository.save(training);
                });
    }
}

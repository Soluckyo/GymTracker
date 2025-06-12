package org.lib.usermanagementservice.kafka;

import org.lib.usermanagementservice.service.ITrainerService;
import org.lib.usermanagementservice.service.TrainerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ITrainerService trainerService;

    public KafkaConsumer(KafkaTemplate<String, String> kafkaTemplate, TrainerService trainerService) {
        this.kafkaTemplate = kafkaTemplate;
        this.trainerService = trainerService;
    }

    @KafkaListener(topics = "trainer-info-request", groupId = "user-management-service")
    public void consumeTrainerInfoRequest(String message) {
        String[] parts = message.split("&");
        UUID trainerId = UUID.fromString(parts[0]);
        String trainingId = UUID.fromString(parts[1]).toString();

        String trainerName = trainerService.findTrainerByUUID(trainerId).getName();

        String response = trainingId + "&" + trainerName;

        kafkaTemplate.send("trainer-info-response", response);
    }
}

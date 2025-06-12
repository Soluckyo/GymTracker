package org.lib.trainingservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendTrainerInfoRequest(UUID trainerId, UUID trainingId) {
        String payload = trainerId.toString() + "&" + trainingId.toString();
        kafkaTemplate.send("trainer-info-request", payload);
    }
}

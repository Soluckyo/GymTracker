package org.lib.trainingservice.repository;

import org.lib.trainingservice.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrainingRepository extends JpaRepository<Training, UUID> {
}

package org.lib.trainingservice.repository;

import org.lib.trainingservice.entity.Training;
import org.lib.trainingservice.entity.TrainingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrainingRepository extends JpaRepository<Training, UUID> {
    Page<Training> findAllByUsersId(UUID usersId, Pageable pageable);

    Page<Training> findAllByType(TrainingType type, Pageable pageable);

    Page<Training> findAllByUsersIdAndType(UUID usersId, TrainingType type, Pageable pageable);
}

package org.lib.trainingservice.repository;

import org.lib.trainingservice.entity.Room;
import org.lib.trainingservice.entity.Training;
import org.lib.trainingservice.entity.TrainingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TrainingRepository extends JpaRepository<Training, UUID> {
    Page<Training> findAllByUsersId(UUID usersId, Pageable pageable);

    Page<Training> findAllByType(TrainingType type, Pageable pageable);

    Page<Training> findAllByUsersIdAndType(UUID usersId, TrainingType type, Pageable pageable);

    @Query("SELECT t FROM Training t " +
            "WHERE t.type = :type AND " +
            "t.room = :room AND " +
            "t.startTime < :endTime AND " +
            "t.endTime > :startTime")
    List<Training> findOverlappingTrainings(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("type") TrainingType type,
            @Param("room") Room room
    );

    @Query("SELECT t FROM Training t " +
            "WHERE t.trainerId = :trainerId AND " +
            "t.startTime < :endTime AND " +
            "t.endTime > :startTime")
    List<Training> findOverlappingTrainingsForTrainer(
            @Param("trainerId") UUID trainerId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}

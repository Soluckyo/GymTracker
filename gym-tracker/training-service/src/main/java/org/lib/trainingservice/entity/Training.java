package org.lib.trainingservice.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Training {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "training_id", nullable = false, updatable = false)
    private UUID trainingId;

    private String title;

    @ElementCollection
    @CollectionTable(name = "training_users", joinColumns = @JoinColumn(name = "subscription_id"))
    @Column(name = "user_id")
    private List<UUID> usersId;

    private UUID trainerId;

    @Enumerated(EnumType.STRING)
    private TrainingType type;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private Room room;

    @Convert(converter = DurationAttributeConverter.class)
    private Duration duration;
}

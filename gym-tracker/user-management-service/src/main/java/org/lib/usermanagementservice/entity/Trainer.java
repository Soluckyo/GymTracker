package org.lib.usermanagementservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "trainer_id", updatable = false, nullable = false)
    private UUID trainerId;

    @Column(nullable = false, unique = true)
    @Email(message = "Пожалуйста введите корректный email")
    private String email;

    @Column(nullable = false, unique = true)
    private String password;

    private String name;

    private String specialization;

    private String workExperience;

    private UUID scheduleId;
}

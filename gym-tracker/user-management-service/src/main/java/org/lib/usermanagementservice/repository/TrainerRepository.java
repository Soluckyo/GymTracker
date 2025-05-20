package org.lib.usermanagementservice.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.lib.usermanagementservice.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, UUID> {
    boolean existsByEmail(@Email @NotBlank(message = "Email не может быть пустым") String email);
}

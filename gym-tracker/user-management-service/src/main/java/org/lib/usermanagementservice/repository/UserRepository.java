package org.lib.usermanagementservice.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.lib.usermanagementservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> getUserByEmail(@Email(message = "Пожалуйста введите корректный email") String email);

    void deleteByEmail(@Email(message = "Пожалуйста введите корректный email") String email);

    boolean existsByEmail(@NotBlank @Email(message = "Пожалуйста введите корректный email") String email);
}

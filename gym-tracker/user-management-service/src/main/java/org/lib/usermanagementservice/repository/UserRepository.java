package org.lib.usermanagementservice.repository;

import jakarta.validation.constraints.Email;
import org.lib.usermanagementservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByEmail(@Email(message = "Пожалуйста введите корректный email") String email);

    void deleteByEmail(@Email(message = "Пожалуйста введите корректный email") String email);
}

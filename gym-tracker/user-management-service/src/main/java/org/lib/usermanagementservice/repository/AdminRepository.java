package org.lib.usermanagementservice.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.lib.usermanagementservice.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {
    boolean existsByEmail(@NotBlank(message = "Email не может быть пустым") @Email String email);

}

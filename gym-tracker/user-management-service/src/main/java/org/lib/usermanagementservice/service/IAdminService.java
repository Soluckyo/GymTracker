package org.lib.usermanagementservice.service;

import org.lib.usermanagementservice.dto.RegistrationAdmin;
import org.lib.usermanagementservice.dto.RegistrationTrainer;
import org.lib.usermanagementservice.dto.RegistrationUser;
import org.lib.usermanagementservice.entity.Admin;
import org.lib.usermanagementservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAdminService {
    Admin registerAdmin(RegistrationAdmin registrationAdmin);
    void registerTrainer(RegistrationTrainer registrationTrainer);
    void registerUser(RegistrationUser registrationUser);
    Page<User> getUsers(Pageable pageable);
    User getUser(String email);
}

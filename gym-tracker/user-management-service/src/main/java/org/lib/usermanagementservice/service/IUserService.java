package org.lib.usermanagementservice.service;

import org.lib.usermanagementservice.dto.RegistrationUser;
import org.lib.usermanagementservice.entity.User;

public interface IUserService {
    void registerUser(RegistrationUser registrationUser);
    public User getUserByEmail(String email);
    public void deleteUserByEmail(String email);
}

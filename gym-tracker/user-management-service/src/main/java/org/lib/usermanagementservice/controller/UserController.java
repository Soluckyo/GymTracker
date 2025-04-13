package org.lib.usermanagementservice.controller;

import org.lib.usermanagementservice.dto.RegistrationUser;
import org.lib.usermanagementservice.entity.User;
import org.lib.usermanagementservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //TODO: добавить логику проверок на ошибки
    @PostMapping("/register-user")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationUser registrationUser) {
        userService.registerUser(registrationUser);
        return ResponseEntity.ok(registrationUser.toString() + "Пользователь успешно зарегистрирован");
    }

    //TODO: переделать на логику поиска по телефону
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<User> getUser(@PathVariable String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/delete/{userEmail}")
    public ResponseEntity<String> deleteUser(@PathVariable String userEmail) {
        userService.deleteUserByEmail(userEmail);
        return ResponseEntity.ok("Пользователь успешно удален");
    }
}

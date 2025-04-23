package org.lib.usermanagementservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name= "user_controller")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //TODO: добавить логику проверок на ошибки
    @Operation(
            summary = "Регистрация пользователя",
            description = "Создает нового пользователя, принимает DTO RegistrationUser"
    )
    @PostMapping("/register-user")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationUser registrationUser) {
        userService.registerUser(registrationUser);
        return ResponseEntity.ok(registrationUser.toString() + "Пользователь успешно зарегистрирован");
    }

    //TODO: переделать на логику поиска по телефону
    @GetMapping("/user/{userEmail}")
    @Operation(
            summary = "Получает пользователя по email",
            description = "Принимает email из пути и из базы достает пользователя"
    )
    public ResponseEntity<User> getUser(@PathVariable String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().body(user);
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Принимает email и удаляет пользователя с соответствующим email'ом"
    )
    @DeleteMapping("/delete/{userEmail}")
    public ResponseEntity<String> deleteUser(@PathVariable String userEmail) {
        userService.deleteUserByEmail(userEmail);
        return ResponseEntity.ok("Пользователь успешно удален");
    }
}

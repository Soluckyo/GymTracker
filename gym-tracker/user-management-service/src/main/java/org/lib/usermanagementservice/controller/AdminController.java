package org.lib.usermanagementservice.controller;


import org.lib.usermanagementservice.dto.RegistrationAdmin;
import org.lib.usermanagementservice.dto.RegistrationTrainer;
import org.lib.usermanagementservice.dto.RegistrationUser;
import org.lib.usermanagementservice.entity.User;
import org.lib.usermanagementservice.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = adminService.getUsers(pageable);
        if (usersPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(usersPage);
    }

    //TODO: переделать на логику поиска по телефону
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<User> getUser(@PathVariable String userEmail) {
        User user = adminService.getUser(userEmail);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().body(user);
    }

    //TODO: добавить проверку на ошибки
    @PostMapping("/register-admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegistrationAdmin registrationAdmin) {
        adminService.registerAdmin(registrationAdmin);
        return ResponseEntity.ok(registrationAdmin.toString() + "Регистрация выполнена успешно");
    }

    //TODO: добавить проверку на ошибки
    @PostMapping("/register-user")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationUser registrationUser) {
        adminService.registerUser(registrationUser);
        return ResponseEntity.ok(registrationUser.toString() + "Пользователь успешно зарегистрирован");
    }

    //TODO: добавить проверку на ошибки
    @PostMapping("/register-trainer")
    public ResponseEntity<String> registerTrainer(@RequestBody RegistrationTrainer registrationTrainer) {
        adminService.registerTrainer(registrationTrainer);
        return ResponseEntity.ok(registrationTrainer.toString() + "Тренер успешно зарегистрирован");
    }
}

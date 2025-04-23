package org.lib.securityservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.lib.securityservice.dto.JwtRequestDTO;
import org.lib.securityservice.dto.JwtResponseDTO;
import org.lib.securityservice.dto.RefreshTokenDTO;
import org.lib.securityservice.dto.RegisterRequest;
import org.lib.securityservice.service.IAppUserService;
import org.lib.securityservice.service.IJwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/auth")
@Tag(name = "auth_controller")
public class AuthController {

    private final IJwtService jwtService;
    private final IAppUserService appUserService;

    public AuthController(IJwtService jwtService, IAppUserService appUserService) {
        this.jwtService = jwtService;
        this.appUserService = appUserService;
    }


    @Operation(
            summary = "Войти в систему",
            description = "Аутентифицирует пользователя"
    )
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> signIn(@RequestBody JwtRequestDTO jwtRequestDTO){
        try{
            JwtResponseDTO jwtResponseDTO = appUserService.signIn(jwtRequestDTO);
            return ResponseEntity.ok(jwtResponseDTO);
        }catch (AuthenticationException e){
            throw new RuntimeException("Authentication failed" + e.getMessage());
        }
    }

    @Operation(
            summary = "Обновить токен",
            description = "Принимает RefreshToken и обновляет AccessToken"
    )
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> refresh(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        try{
            JwtResponseDTO jwtResponseDTO = appUserService.refreshToken(refreshTokenDTO);
            return ResponseEntity.ok(jwtResponseDTO);
        }catch (Exception e){
            throw new RuntimeException("failed" + e.getMessage());
        }
    }

    @Operation(
            summary = "Создание сущности AppUser",
            description = "Создает новую сущность AppUser на основе присланного пользователя из user-management-service"
    )
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        appUserService.register(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Валидация токена (только для внутреннего пользования из других микросервисов)",
            description = "Проверяет валиден ли токен. Берет токен из Header authorization"
    )
    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String authHeader) {
        return jwtService.extractUserInfoFromToken(authHeader);
    }

}

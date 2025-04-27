package org.lib.securityservice.service;

import org.lib.securityservice.dto.JwtRequestDTO;
import org.lib.securityservice.dto.JwtResponseDTO;
import org.lib.securityservice.dto.RefreshTokenDTO;
import org.lib.securityservice.dto.RegisterRequest;
import org.lib.securityservice.entity.AppUser;
import org.lib.securityservice.exception.EmailAlreadyExistsException;
import org.lib.securityservice.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
public class AppUserService implements IAppUserService {

    private final AppUserRepository appUserRepository;
    private final IJwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, IJwtService jwtService, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    public JwtResponseDTO signIn(JwtRequestDTO jwtRequestDTO) throws AuthenticationException {
        AppUser user = findByCredentials(jwtRequestDTO);
        return jwtService.generateAuthToken(user);
    }

    public JwtResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO) throws Exception {
        String refreshToken = refreshTokenDTO.getRefreshToken();
        if(refreshToken != null && jwtService.validateToken(refreshToken)){
            AppUser user = findByEmail(jwtService.getEmailFromToken(refreshToken));
            return jwtService.refreshBaseToken(user, refreshToken);
        }throw new AuthenticationException("Невалидный refresh токен");
    }

    private AppUser findByCredentials(JwtRequestDTO jwtRequestDTO) throws AuthenticationException {
        Optional<AppUser> userOptional = appUserRepository.findByEmail(jwtRequestDTO.getEmail());
        if(userOptional.isPresent()) {
            AppUser user = userOptional.get();
            if(passwordEncoder.matches(jwtRequestDTO.getPassword(), user.getPassword())) {
                return user;
            }
        } throw new AuthenticationException("Email или пароль не корректные");
    }

    private AppUser findByEmail(String email) throws Exception {
        return appUserRepository.findByEmail(email).orElseThrow(()-> new Exception(String.format("Email %s не найден", email)));
    }

    public void register(RegisterRequest request) {
        if (appUserRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Такой Email уже используется");
        }

        AppUser appUser = AppUser.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .externalUserId(request.getExternalUserId())
                .build();

        appUserRepository.save(appUser);
    }
}

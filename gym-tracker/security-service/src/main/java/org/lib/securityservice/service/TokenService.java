package org.lib.securityservice.service;

import org.lib.securityservice.entity.AppUser;
import org.lib.securityservice.entity.RefreshToken;
import org.lib.securityservice.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenService implements ITokenService{

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void saveRefreshToken(AppUser appUser, String token, long ttlMinutes) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(appUser)
                .expiryDate(LocalDateTime.now().plusMinutes(ttlMinutes))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    public void revokeRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(
                refreshToken -> {
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
                }
            );
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}

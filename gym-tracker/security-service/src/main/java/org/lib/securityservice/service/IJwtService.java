package org.lib.securityservice.service;

import io.jsonwebtoken.Claims;
import org.lib.securityservice.dto.JwtResponseDTO;
import org.lib.securityservice.entity.AppUser;

import javax.crypto.SecretKey;

public interface IJwtService {
    String generateAccessToken(AppUser user);
    SecretKey getSignInKey();
    String generateRefreshToken(AppUser user);
    Boolean validateToken(String token);
    JwtResponseDTO generateAuthToken(AppUser user);
    JwtResponseDTO refreshBaseToken(AppUser user, String refreshToken);
    Claims claimsFromToken(String token);
    String getEmailFromToken(String token);
    String getRoleFromToken(String token);
}

package org.lib.securityservice.service;

import org.lib.securityservice.dto.JwtRequestDTO;
import org.lib.securityservice.dto.JwtResponseDTO;
import org.lib.securityservice.dto.RefreshTokenDTO;
import org.lib.securityservice.dto.RegisterRequest;
import org.lib.securityservice.entity.AppUser;

import javax.naming.AuthenticationException;

public interface IAppUserService {
    JwtResponseDTO signIn(JwtRequestDTO jwtRequestDTO) throws AuthenticationException;
    JwtResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO) throws Exception;
    private AppUser findByCredentials(JwtRequestDTO jwtRequestDTO) throws AuthenticationException{return null;};
    void register(RegisterRequest request);

}

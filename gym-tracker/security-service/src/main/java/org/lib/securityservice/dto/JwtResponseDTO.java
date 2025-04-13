package org.lib.securityservice.dto;

import lombok.Data;

@Data
public class JwtResponseDTO {
    private String token;
    private String refreshToken;
}

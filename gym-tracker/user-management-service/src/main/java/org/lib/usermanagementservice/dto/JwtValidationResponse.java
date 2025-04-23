package org.lib.usermanagementservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtValidationResponse {
    String email;
    String role;
}

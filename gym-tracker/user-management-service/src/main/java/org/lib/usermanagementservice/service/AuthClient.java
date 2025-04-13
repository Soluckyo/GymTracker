package org.lib.usermanagementservice.service;

import org.lib.usermanagementservice.dto.RegisterAppUserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthClient {
    private final RestTemplate restTemplate;
    private final String securityServiceUrl;

    public AuthClient(RestTemplate restTemplate,
                      @Value("${security.service.url}") String securityServiceUrl) {
        this.restTemplate = restTemplate;
        this.securityServiceUrl = securityServiceUrl;
    }

    public void registerAppUser(RegisterAppUserRequest registerAppUserRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterAppUserRequest> entity = new HttpEntity<>(registerAppUserRequest, headers);
        restTemplate.postForEntity(securityServiceUrl + "/auth/register", entity, Void.class);
    }


}

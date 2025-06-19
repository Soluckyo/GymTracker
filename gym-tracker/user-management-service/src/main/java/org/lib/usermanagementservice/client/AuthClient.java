package org.lib.usermanagementservice.client;

import org.lib.usermanagementservice.dto.RegisterAppUserRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuthClient {
    private final WebClient webClient;

    public AuthClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public void registerAppUser(RegisterAppUserRequest request) {
        webClient.post()
                .uri("http://security-service/auth/register")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }


}

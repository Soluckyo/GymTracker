package org.lib.trainingservice.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.lib.trainingservice.dto.JwtValidationResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class TokenValidationFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private UsernamePasswordAuthenticationToken authenticationToken;

    public TokenValidationFilter(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Не найден или невалидный Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        String securityServiceUrl = "http://security-service/auth/validate";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //отправляем запрос в security-service на валидность токена
        try{
            ResponseEntity<String> validationResponse = restTemplate.exchange(
                    securityServiceUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            //если токен валиден то, кладем в контекст пользователя
            if(validationResponse.getStatusCode().is2xxSuccessful()) {
                authenticationToken = prepareAuthenticationToken(validationResponse);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("Пользователь успешно авторизован");
            }else{
                log.warn("Token validation failed: {}", validationResponse.getStatusCode());
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Token validation error", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    public UsernamePasswordAuthenticationToken prepareAuthenticationToken(ResponseEntity<String> validationResponse) {
        String body = validationResponse.getBody();
        JwtValidationResponse jwtInfo = null;
        try {
            jwtInfo = objectMapper.readValue(body, JwtValidationResponse.class);
            log.info("Аутентификация пользователя: {}, role: {}", jwtInfo.getEmail(), jwtInfo.getRole());
        } catch (JsonProcessingException e) {
            log.error("Не удалось парсинга ответа на проверку", e);
            throw new RuntimeException(e);
        }
        List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + jwtInfo.getRole())
            );

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    jwtInfo.getEmail(), null, authorities
            );


            authenticationToken.setDetails(jwtInfo.getUserId());

            return authenticationToken;
    }
}

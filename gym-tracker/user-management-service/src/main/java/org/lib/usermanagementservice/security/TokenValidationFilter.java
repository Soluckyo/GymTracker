package org.lib.usermanagementservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.lib.usermanagementservice.dto.TokenValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.Key;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class TokenValidationFilter extends OncePerRequestFilter {

    private final WebClient webClient;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private UsernamePasswordAuthenticationToken authenticationToken;

    @Value("${jwt.secret}")
    private String secret;
    private final Key jwtSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));


    public TokenValidationFilter(RestTemplate restTemplate, ObjectMapper objectMapper, WebClient webClient) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.webClient = webClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            TokenValidationResponse tokenValidationResponse = validateToken(token);
            String role = tokenValidationResponse.getRole();
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            tokenValidationResponse.getEmail(),
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                            );
            authToken.setDetails(tokenValidationResponse.getUserId());

            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        }catch (Exception e) {
            log.error("Ошибка валидации токена: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


    private TokenValidationResponse validateToken(String token) {
        try{
            return webClient.get()
                    .uri("auth/validate")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .onStatus(
                            httpStatusCode -> !httpStatusCode.is2xxSuccessful(),
                            clientResponse -> {
                                log.error("Ошибка валидации токена. Статус: {}", clientResponse.statusCode());
                                return Mono.error(new SecurityException("Токен не валиден"));
                            }
                    )
                    .bodyToMono(TokenValidationResponse.class)
                    .timeout(Duration.ofMillis(500))
                    .block();
        }catch (Exception e){
            log.warn("Удаленная валидация не сработала, вызываем локальную валидацию");
            return validateTokenLocally(token);
        }
    }

    private TokenValidationResponse validateTokenLocally(String token){
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return TokenValidationResponse.builder()
                    .userId(UUID.fromString(claims.get("userId", String.class)))
                    .email(claims.getSubject())
                    .role(claims.get("role", String.class))
                    .build();
        } catch (Exception e) {
            throw new SecurityException("Не удалось провалидировать токен");
        }
    }
}

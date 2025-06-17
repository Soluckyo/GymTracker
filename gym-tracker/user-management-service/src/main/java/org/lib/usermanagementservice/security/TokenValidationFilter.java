package org.lib.usermanagementservice.security;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.lib.usermanagementservice.dto.JwtValidationResponse;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TokenValidationFilter extends OncePerRequestFilter {

    private final WebClient webClient;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private UsernamePasswordAuthenticationToken authenticationToken;

    @Value("${jwt.secret}")
    private String secret;


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
            Mono<Boolean> validationMono = webClient.get()
                    .uri("auth/validate")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .onStatus(
                            httpStatusCode -> !httpStatusCode.is2xxSuccessful(),
                            clientResponse -> {
                                log.error("Ошибка валидации токена. Статус: {}", clientResponse.statusCode());
                                return Mono.error(new SecurityException("Токен не валиден")):
                            }
                    )
                    .bodyToMono(JwtValidationResponse.class)
                    .timeout(Duration.ofMillis(500))
                    .subscribe(
                            tokenResponse -> {
                                // Успешная валидация
                                UsernamePasswordAuthenticationToken auth =
                                        new UsernamePasswordAuthenticationToken(
                                                tokenResponse.getEmail(),
                                                null,
                                                List.of(new SimpleGrantedAuthority("ROLE_" + tokenResponse.getRole()))
                                        );
                                auth.setDetails(tokenResponse.getUserId());
                                SecurityContextHolder.getContext().setAuthentication(auth);
                                filterChain.doFilter(request, response);
                            },
                            error -> {
                                // Обработка ошибок
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                log.error("Ошибка при валидации токена", error);
                            }
                    );
        }catch (Exception e){
            log.error(e.getMessage());
        }


        Boolean isValid = validationMono.block();

        if (Boolean.TRUE.equals(isValid)) {
            SecurityContextHolder.getContext().setAuthentication(createAuth(token));
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    private boolean validateTokenLocally(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public UsernamePasswordAuthenticationToken createAuth(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role").toString()))
        );
    }
}

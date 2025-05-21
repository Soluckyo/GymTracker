package org.lib.securityservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.lib.securityservice.dto.JwtResponseDTO;
import org.lib.securityservice.dto.TokenValidationResponse;
import org.lib.securityservice.entity.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class JwtService implements IJwtService {

    private final TokenService tokenService;
    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.access_expiration_minutes}")
    private Long ACCESS_EXPIRATION_MINUTES;

    @Value("${jwt.refresh_expiration_days}")
    private Long REFRESH_EXPIRATION_DAYS;

    public JwtService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    //Генерация Access токена
    public String generateAccessToken(AppUser user) {
        Date dateEx = Date.from(LocalDateTime.now().plusMinutes(ACCESS_EXPIRATION_MINUTES)
                .atZone(ZoneId.systemDefault()).toInstant());
        Date now = new Date();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .claim("userId", user.getExternalUserId().toString())
                .setIssuedAt(now)
                .setExpiration(dateEx)
                .signWith(getSignInKey())
                .compact();
    }

    //Получение секрета
    public SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }

    //Генерация refresh токена
    public String generateRefreshToken(AppUser user) {
        Date dateEx = Date.from(LocalDateTime.now().plusDays(REFRESH_EXPIRATION_DAYS)
                .atZone(ZoneId.systemDefault()).toInstant());
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(dateEx)
                .signWith(getSignInKey())
                .compact();

        tokenService.saveRefreshToken(user, refreshToken, REFRESH_EXPIRATION_DAYS);

        return refreshToken;
    }

    //проверка правильности токена
    /**
     * Валидирует токен
     *
     * @param token токен доступа или токен обновления
     * @return true, если токен валидный
     *         false, если токен невалидный
     */
    public Boolean validateToken(String token) {
        claimsFromToken(token);
        log.debug("Попытка валидировать токен: {}", token);
        return true;
    }

    //Декодирование токена и получение данных
    public ResponseEntity<?> extractUserInfoFromToken(String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Недопустимый заголовок авторизации: {}", authHeader);
            return ResponseEntity.badRequest().body("Недопустимый заголовок авторизации");
        }

        log.info("Заголовок авторизации: {}", authHeader);

        String token = authHeader.substring(7);

        if(!validateToken(token)) {
            log.info("Токен не валиден: {}", token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный или просроченный токен");
        }

        log.info("Прошел if");

        UUID userId = UUID.fromString(getUserIdFromToken(token));
        log.info("Получили id из токена: {}", userId);
        String email = getEmailFromToken(token);
        log.info("Получили email из токена: {}", email);
        String role = getRoleFromToken(token);
        log.info("Получили из role токена: {}", role);

        TokenValidationResponse tokenValidationResponse = new TokenValidationResponse();
        tokenValidationResponse.setUserId(userId);
        tokenValidationResponse.setEmail(email);
        tokenValidationResponse.setRole(role);

        log.info("Данные отправлены: {}", tokenValidationResponse);
        return ResponseEntity.ok(tokenValidationResponse);
    }

    //генерация аутентификационного токена(складываем токены в dto, чтобы потом отдать)
    public JwtResponseDTO generateAuthToken(AppUser user) {
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setToken(generateAccessToken(user));
        jwtResponseDTO.setRefreshToken(generateRefreshToken(user));
        return jwtResponseDTO;
    }

    //обновление jwt токена с помощью refresh токена
    public JwtResponseDTO refreshBaseToken(AppUser user, String refreshToken) {
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setToken(generateAccessToken(user));
        jwtResponseDTO.setRefreshToken(refreshToken);
        return jwtResponseDTO;
    }

    //достаем claims из токена
    public Claims claimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Достает Email токена доступа
     * @param token токен доступа
     * @return строку с Email
     */
    public String getEmailFromToken(String token) {
        return claimsFromToken(token).getSubject();
    }

    /**
     * Достает роль пользователя из токена
     * @param token токен доступа
     * @return строку с ролью
     */
    public String getRoleFromToken(String token) {
        return claimsFromToken(token).get("role", String.class);
    }

    /**
     * Достает из токена userId в виде строки
     * @param token токен доступа
     * @return Строку с UUID пользователя
     */
    public String getUserIdFromToken(String token) {
        return claimsFromToken(token).get("userId", String.class);
    }
}

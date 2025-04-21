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

@Slf4j
@Service
public class JwtService implements IJwtService {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.access_expiration_minutes}")
    private Long ACCESS_EXPIRATION_MINUTES;

    @Value("${jwt.refresh_expiration_days}")
    private Long REFRESH_EXPIRATION_DAYS;

    //Генерация Access токена
    public String generateAccessToken(AppUser user) {
        Date dateEx = Date.from(LocalDateTime.now().plusMinutes(ACCESS_EXPIRATION_MINUTES)
                .atZone(ZoneId.systemDefault()).toInstant());
        Date now = new Date();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
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
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(dateEx)
                .signWith(getSignInKey())
                .compact();
    }

    //проверка правильности токена
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        }catch (ExpiredJwtException expEx) {
                log.error("Token expired", expEx);
                return false;
            } catch (UnsupportedJwtException unsEx) {
                log.error("Unsupported jwt", unsEx);
                return false;
            } catch (MalformedJwtException mjEx) {
                log.error("Malformed jwt", mjEx);
                return false;
            } catch (SignatureException sEx) {
                log.error("Invalid signature", sEx);
                return false;
            } catch (Exception e) {
                log.error("invalid token", e);
                return false;
            }
    }

    //Декодирование токена и получение данных
    public ResponseEntity<?> extractUserInfoFromToken(String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Недопустимый заголовок авторизации");
        }

        String token = authHeader.substring(7);

        if(!validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный или просроченный токен");
        }

        String email = getEmailFromToken(token);
        String role = getRoleFromToken(token);

        TokenValidationResponse tokenValidationResponse = new TokenValidationResponse();
        tokenValidationResponse.setEmail(email);
        tokenValidationResponse.setRole(role);

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

    //получаем email
    public String getEmailFromToken(String token) {
        return claimsFromToken(token).getSubject();
    }

    //получаем role
    public String getRoleFromToken(String token) {
        return claimsFromToken(token).get("role", String.class);
    }
}

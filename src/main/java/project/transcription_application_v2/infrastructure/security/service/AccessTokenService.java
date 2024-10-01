package project.transcription_application_v2.infrastructure.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.domain.user.service.UserService;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessTokenService {

  @Value("${security.jwt.token}")
  private String secretKey;

  @Value("${security.jwt.expiration}")
  private Long tokenDurationMs;

  private final UserService userService;

  public String getJWTFromRequest(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    return StringUtils.hasText(token) && token.startsWith("Bearer ") ? token.replace("Bearer ", "") : "";
  }

  public String create(Long userId) throws NotFoundException {

    Date now = new Date(System.currentTimeMillis());

    Date expiryDate = new Date(now.getTime() + tokenDurationMs);

    User user = userService.findById(userId);

    Map<String, Object> claims = new HashMap<>();
    claims.put("username", user.getUsername());
    claims.put("role", user.getRole().name());

    return Jwts.builder()
        .subject(String.valueOf(userId))
        .claims(claims)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getKey())
        .compact();
  }

  public boolean validate(String token) {
    try {
      Jwts
          .parser()
          .verifyWith(getKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  public Claims extractClaims(String token) {
    return Jwts.
        parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String extractUsername(String token) {
    return extractClaims(token).get("username", String.class);
  }

  private SecretKey getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}

package project.transcription_application_v2.infrastructure.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.domain.user.service.UserService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

  private static final Logger logger = LoggerFactory.getLogger(AccessTokenService.class);

  @Value("${security.jwt.token}")
  private String secretKey;

  @Value("${security.jwt.expiration}")
  private Long tokenDurationMs;

  private final UserService userService;

  public String getJWTFromRequest(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    return StringUtils.hasText(token) && token.startsWith("Bearer ") ? token.replace("Bearer ", "") : "";
  }

  public String create(Long userId) {

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
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
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

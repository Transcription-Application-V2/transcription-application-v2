package project.transcription_application_v2.infrastructure.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.domain.user.service.UserService;
import project.transcription_application_v2.infrastructure.exceptions.RefreshTokenException;
import project.transcription_application_v2.infrastructure.security.entity.RefreshToken;
import project.transcription_application_v2.infrastructure.security.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  @Value("${security.jwt.rt.expiration}")
  private Long refreshTokenDurationMs;

  private final UserService userService;


  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(Long userId) {
    RefreshToken refreshToken = new RefreshToken(
        UUID.randomUUID().toString(),
        Instant.now().plusMillis(refreshTokenDurationMs),
        userService.findById(userId));

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public void verifyExpiration(RefreshToken token) throws RefreshTokenException {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new RefreshTokenException(token.getToken(), "Refresh token was expired. Please make a new sign-in request");
    }
  }

  @Transactional
  public void deleteByUserId(User user) {
    refreshTokenRepository.deleteByUser(user);
  }

}

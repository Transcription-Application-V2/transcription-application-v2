package project.transcription_application_v2.infrastructure.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

  @Value("${security.jwt.rt.expiration}")
  private Long refreshTokenDurationMs;

  private final RefreshTokenRepository refreshTokenRepository;

  private final UserService userService;

  @Transactional
  public RefreshToken createRefreshToken(Long userId) {
    Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserId(userId);

    // Delete already existing RefreshToken connected to the User
    // Flush is necessary because the block is marked as @Transactional
    // and changes won't persist until the whole block is executed
    if (existingToken.isPresent()) {
      this.deleteByUserId(userId);
      refreshTokenRepository.flush();
    }

    RefreshToken refreshToken = new RefreshToken(
        UUID.randomUUID().toString(),
        Instant.now().plusMillis(refreshTokenDurationMs),
        userService.findById(userId));

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public void verifyExpiration(RefreshToken token) throws RefreshTokenException {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new RefreshTokenException(token.getToken(), "Refresh token was expired. Please make a new sign-in request");
    }
  }

  @Transactional
  public void deleteByUserId(Long id) {
    refreshTokenRepository.deleteByUser(userService.findById(id));
  }

}

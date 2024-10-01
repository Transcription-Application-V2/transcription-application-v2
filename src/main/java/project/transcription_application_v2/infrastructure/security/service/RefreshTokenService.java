package project.transcription_application_v2.infrastructure.security.service;

import static project.transcription_application_v2.infrastructure.exceptions.ExceptionMessages.REFRESH_TOKEN_EXPIRED;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transcription_application_v2.domain.user.service.UserService;
import project.transcription_application_v2.infrastructure.exceptions.throwable.ForbiddenException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.security.entity.RefreshToken;
import project.transcription_application_v2.infrastructure.security.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  @Value("${security.jwt.rt.expiration}")
  private Long refreshTokenDurationMs;

  private final RefreshTokenRepository refreshTokenRepository;

  private final UserService userService;

  @Transactional
  public RefreshToken createRefreshToken(Long userId) throws NotFoundException {
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

  public void verifyExpiration(RefreshToken token) throws ForbiddenException {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new ForbiddenException(REFRESH_TOKEN_EXPIRED);
    }
  }

  @Transactional
  public void deleteByUserId(Long id) throws NotFoundException {
    refreshTokenRepository.deleteByUser(userService.findById(id));
  }

}

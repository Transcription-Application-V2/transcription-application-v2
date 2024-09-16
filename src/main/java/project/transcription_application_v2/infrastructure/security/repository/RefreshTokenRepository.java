package project.transcription_application_v2.infrastructure.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.infrastructure.security.entity.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByToken(String token);

  @Modifying
  int deleteByUser(User user);

}

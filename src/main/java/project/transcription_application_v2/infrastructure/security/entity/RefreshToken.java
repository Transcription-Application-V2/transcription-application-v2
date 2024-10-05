package project.transcription_application_v2.infrastructure.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import project.transcription_application_v2.domain.BaseEntity;
import project.transcription_application_v2.domain.user.entity.User;

import java.time.Instant;

@Entity(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true , exclude = {"user"})
@ToString(callSuper = true, exclude = {"user"})
public class RefreshToken extends BaseEntity {

  @Column(unique = true, nullable = false)
  private String token;

  @Column(nullable = false)
  private Instant expiryDate;

  @OneToOne
  private User user;
}

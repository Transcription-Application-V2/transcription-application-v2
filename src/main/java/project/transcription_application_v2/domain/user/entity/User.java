package project.transcription_application_v2.domain.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import project.transcription_application_v2.domain.BaseEntity;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.user.enums.RoleName;
import project.transcription_application_v2.infrastructure.security.entity.RefreshToken;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"files" , "refreshToken"})
@EqualsAndHashCode(callSuper = true, exclude = {"files" , "refreshToken"})
public class User extends BaseEntity {

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  @Email
  private String email;

  @Column
  private String firstName;
  @Column
  private String lastName;

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
  @Column
  private LocalDateTime updatedAt = LocalDateTime.now();

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private RoleName role = RoleName.USER;

  @OneToMany(mappedBy = "user")
  private List<File> files = new ArrayList<>();

  @OneToOne(mappedBy = "user" , orphanRemoval = true)
  private RefreshToken refreshToken;
}

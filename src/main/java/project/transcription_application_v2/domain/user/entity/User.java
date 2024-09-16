package project.transcription_application_v2.domain.user.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.transcription_application_v2.domain.BaseEntity;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.user.enums.RoleName;
import project.transcription_application_v2.infrastructure.security.entity.RefreshToken;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
  private LocalDateTime createdAt;
  @Column
  private LocalDateTime updatedAt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private RoleName role;

  @OneToMany(mappedBy = "user")
  private List<File> files;

}

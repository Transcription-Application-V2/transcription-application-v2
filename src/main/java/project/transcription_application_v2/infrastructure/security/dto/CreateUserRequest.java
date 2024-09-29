package project.transcription_application_v2.infrastructure.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import project.transcription_application_v2.domain.user.annotations.email.EmailUnique;
import project.transcription_application_v2.domain.user.annotations.username.UsernameUnique;

@Data
public class CreateUserRequest {

  @NotNull
  @Size(min = 5, max = 25, message = "Username must be between 5 and 25 characters.")
  @UsernameUnique
  private String username;

  @NotNull
  @Size(min = 5, max = 50, message = "Password must be between 5 and 50 characters.")
  private String password;

  @NotNull(message = "Email is required.")
  @Email(message = "Email should be valid.")
  @EmailUnique
  private String email;

  @Size(max = 255)
  private String firstName;
  @Size(max = 255)
  private String lastName;

}

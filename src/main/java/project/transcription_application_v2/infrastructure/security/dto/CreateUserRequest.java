package project.transcription_application_v2.infrastructure.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

  @NotNull
  @Size(min = 5, max = 25, message = "Username must be between 5 and 25 characters.")
  private String username;

  @NotNull
  @Size(min = 5, max = 50, message = "Password must be between 5 and 50 characters.")
  private String password;

  @NotNull(message = "Email is required.")
  @Email(message = "Email should be valid.")
  private String email;

  @Size(max = 255)
  private String firstName;
  @Size(max = 255)
  private String lastName;

}

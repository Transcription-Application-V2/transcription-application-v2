package project.transcription_application_v2.infrastructure.security.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequest {

  @NotNull
  @Size(min =5, max = 50)
  private String username;

  @NotNull
  @Size(min =5, max = 50)
  private String password;

}

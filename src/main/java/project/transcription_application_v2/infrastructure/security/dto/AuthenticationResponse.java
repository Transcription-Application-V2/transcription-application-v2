package project.transcription_application_v2.infrastructure.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationResponse {

  private String token;

  private String refreshToken;
}

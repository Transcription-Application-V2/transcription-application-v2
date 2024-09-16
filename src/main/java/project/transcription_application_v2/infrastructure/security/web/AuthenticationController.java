package project.transcription_application_v2.infrastructure.security.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.exceptions.RefreshTokenException;
import project.transcription_application_v2.infrastructure.security.dto.AuthenticationRequest;
import project.transcription_application_v2.infrastructure.security.dto.AuthenticationResponse;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;
import project.transcription_application_v2.infrastructure.security.dto.RefreshTokenRequest;
import project.transcription_application_v2.infrastructure.security.service.AuthenticationService;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws BadResponseException {

    return ResponseEntity
        .ok()
        .body(authenticationService.authenticate(authenticationRequest));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws RefreshTokenException {

    return ResponseEntity
        .ok()
        .body(authenticationService.refreshToken(refreshTokenRequest.getRefreshToken()));
  }

  @PostMapping("/invalidate")
  public ResponseEntity<MessageResponse> invalidate(){

    return ResponseEntity
        .ok()
        .body(authenticationService.signOut());
  }

}

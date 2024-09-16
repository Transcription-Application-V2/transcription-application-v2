package project.transcription_application_v2.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.transcription_application_v2.domain.user.service.UserService;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.security.dto.CreateUserRequest;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;

@RestController
@RequestMapping("/api/v2/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/create")
  public ResponseEntity<MessageResponse> handleSignUp(@RequestBody CreateUserRequest createUserRequest) throws BadResponseException {

    return ResponseEntity
        .ok()
        .body(userService.create(createUserRequest));
  }
}

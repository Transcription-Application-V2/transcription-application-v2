package project.transcription_application_v2.infrastructure.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class RefreshTokenException extends Throwable{

  public RefreshTokenException(String token, String message) {
    super(String.format("Failed for [%s]: %s", token, message));
  }
}

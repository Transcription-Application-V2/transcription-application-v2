package project.transcription_application_v2.infrastructure.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends Throwable{

  private final String message;

  public NotFoundException(String message) {
    this.message = message;
  }
}

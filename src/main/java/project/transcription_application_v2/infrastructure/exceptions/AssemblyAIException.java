package project.transcription_application_v2.infrastructure.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AssemblyAIException extends Throwable {

  private final String message;

  public AssemblyAIException(String message) {
    this.message = message;
  }
}

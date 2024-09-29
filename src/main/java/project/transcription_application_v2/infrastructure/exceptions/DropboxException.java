package project.transcription_application_v2.infrastructure.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DropboxException extends Throwable implements GeneralException{

  private final String message;

  public DropboxException(String message) {
    this.message = message;
  }

  @Override
  public String getTitle() {
    return "Dropbox Error";
  }
}

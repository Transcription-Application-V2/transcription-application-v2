package project.transcription_application_v2.infrastructure.exceptions.throwable;

import lombok.Getter;
import project.transcription_application_v2.infrastructure.exceptions.ExceptionMessages;

@Getter
public abstract class BaseException extends Throwable {

  private final ExceptionMessages exceptionMessage;

  public BaseException(ExceptionMessages exceptionMessage, Object... values) {
    super(String.format(exceptionMessage.message, values));
    this.exceptionMessage = exceptionMessage;
  }

  public BaseException(ExceptionMessages exceptionMessage) {
    super(exceptionMessage.message);
    this.exceptionMessage = exceptionMessage;
  }

  public String getTitle() {
    return exceptionMessage.title;
  }
}
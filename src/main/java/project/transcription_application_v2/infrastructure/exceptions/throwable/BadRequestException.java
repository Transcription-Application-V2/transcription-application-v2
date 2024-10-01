package project.transcription_application_v2.infrastructure.exceptions.throwable;

import project.transcription_application_v2.infrastructure.exceptions.ExceptionMessages;

public class BadRequestException extends BaseException {

  public BadRequestException(ExceptionMessages exceptionMessage, Object... values) {
    super(exceptionMessage, values);
  }

  public BadRequestException(ExceptionMessages exceptionMessage) {
    super(exceptionMessage);
  }
}

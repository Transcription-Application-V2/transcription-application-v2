package project.transcription_application_v2.infrastructure.exceptions.throwable;

import project.transcription_application_v2.infrastructure.exceptions.ExceptionMessages;

public class ForbiddenException extends BaseException {

  public ForbiddenException(ExceptionMessages exceptionMessage, Object... values) {
    super(exceptionMessage, values);
  }

  public ForbiddenException(ExceptionMessages exceptionMessage) {
    super(exceptionMessage);
  }
}

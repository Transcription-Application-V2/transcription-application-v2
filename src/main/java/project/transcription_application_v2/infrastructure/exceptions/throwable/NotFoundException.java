package project.transcription_application_v2.infrastructure.exceptions.throwable;

import project.transcription_application_v2.infrastructure.exceptions.ExceptionMessages;

public class NotFoundException extends BaseException {

  public NotFoundException(ExceptionMessages exceptionMessage, Object... values) {
    super(exceptionMessage, values);
  }

  public NotFoundException(ExceptionMessages exceptionMessage) {
    super(exceptionMessage);
  }
}

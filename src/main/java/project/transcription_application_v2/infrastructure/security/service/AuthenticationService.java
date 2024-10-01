package project.transcription_application_v2.infrastructure.security.service;

import project.transcription_application_v2.infrastructure.exceptions.throwable.BadRequestException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.ForbiddenException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.security.dto.AuthenticationRequest;
import project.transcription_application_v2.infrastructure.security.dto.AuthenticationResponse;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;

public interface AuthenticationService {

  AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest)
      throws BadRequestException;

  AuthenticationResponse refreshToken(String refreshToken)
      throws ForbiddenException, NotFoundException;

  MessageResponse signOut();


}

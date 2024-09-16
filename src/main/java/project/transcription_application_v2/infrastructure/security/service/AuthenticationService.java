package project.transcription_application_v2.infrastructure.security.service;

import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.exceptions.RefreshTokenException;
import project.transcription_application_v2.infrastructure.security.dto.AuthenticationRequest;
import project.transcription_application_v2.infrastructure.security.dto.AuthenticationResponse;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;

public interface AuthenticationService {

  AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws BadResponseException;

  AuthenticationResponse refreshToken(String refreshToken) throws RefreshTokenException;

  MessageResponse signOut();


}

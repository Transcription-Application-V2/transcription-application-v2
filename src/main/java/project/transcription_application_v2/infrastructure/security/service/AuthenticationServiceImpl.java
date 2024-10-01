package project.transcription_application_v2.infrastructure.security.service;

import static project.transcription_application_v2.infrastructure.exceptions.ExceptionMessages.AUTHENTICATION_FAILED;
import static project.transcription_application_v2.infrastructure.exceptions.ExceptionMessages.REFRESH_TOKEN_EXPIRED;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.infrastructure.exceptions.throwable.BadRequestException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.ForbiddenException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.security.dto.AuthenticationRequest;
import project.transcription_application_v2.infrastructure.security.dto.AuthenticationResponse;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;
import project.transcription_application_v2.infrastructure.security.entity.RefreshToken;
import project.transcription_application_v2.infrastructure.security.entity.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


  private final AccessTokenService accessTokenService;
  private final RefreshTokenService refreshTokenService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest)
      throws BadRequestException {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      SecurityContextHolder.getContext().setAuthentication(authentication);

      String token = accessTokenService.create(userDetails.getUser().getId());

      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser().getId());

      return new AuthenticationResponse(token, refreshToken.getToken());
    } catch (Exception | NotFoundException exception) {
      throw new BadRequestException(AUTHENTICATION_FAILED, exception.getMessage());
    }
  }

  public AuthenticationResponse refreshToken(String refreshToken)
      throws ForbiddenException, NotFoundException {
    Optional<RefreshToken> existingToken = refreshTokenService.findByToken(refreshToken);

    if (existingToken.isPresent()) {
      refreshTokenService.verifyExpiration(existingToken.get());

      Long userId = existingToken.get().getUser().getId();

      String token = accessTokenService.create(userId);

      RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(userId);

      return new AuthenticationResponse(token, newRefreshToken.getToken());
    }
    throw new ForbiddenException(REFRESH_TOKEN_EXPIRED);
  }

  public MessageResponse signOut() {
    try {
      User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      refreshTokenService.deleteByUserId(user.getId());

      SecurityContextHolder.getContext().setAuthentication(null);

      return new MessageResponse("Successfully signed out.");
    } catch (Exception | NotFoundException exception) {
      return new MessageResponse("Signing out failed: {}" + exception.getMessage());
    }
  }
}

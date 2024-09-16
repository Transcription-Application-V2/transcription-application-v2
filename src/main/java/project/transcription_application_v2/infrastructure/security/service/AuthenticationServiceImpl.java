package project.transcription_application_v2.infrastructure.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.exceptions.RefreshTokenException;
import project.transcription_application_v2.infrastructure.security.dto.AuthenticationRequest;
import project.transcription_application_v2.infrastructure.security.dto.AuthenticationResponse;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;
import project.transcription_application_v2.infrastructure.security.entity.RefreshToken;
import project.transcription_application_v2.infrastructure.security.entity.UserDetailsImpl;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


  private final AccessTokenService accessTokenService;
  private final RefreshTokenService refreshTokenService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws BadResponseException {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      SecurityContextHolder.getContext().setAuthentication(authentication);

      String token = accessTokenService.create(userDetails.getUser().getId());

      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser().getId());

      return new AuthenticationResponse(token, refreshToken.getToken());
    } catch (Exception exception) {
      throw new BadResponseException(exception.getMessage());
    }
  }

  public AuthenticationResponse refreshToken(String refreshToken) throws RefreshTokenException {
    Optional<RefreshToken> rt = refreshTokenService.findByToken(refreshToken);

    if (rt.isPresent()) {
      refreshTokenService.verifyExpiration(rt.get());

      Long userId = rt.get().getUser().getId();

      String token = accessTokenService.create(userId);

      refreshTokenService.deleteByUserId(userId);

      RefreshToken newRt = refreshTokenService.createRefreshToken(userId);

      return new AuthenticationResponse(token, newRt.getToken());
    }
    throw new RefreshTokenException(refreshToken, "Refresh token was expired. Please make a new sign-in request");
  }

  public MessageResponse signOut() {
    try {
      User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      refreshTokenService.deleteByUserId(user.getId());

      SecurityContextHolder.getContext().setAuthentication(null);

      return new MessageResponse("Successfully signed out.");
    } catch (Exception exception) {
      return new MessageResponse(exception.getMessage());
    }
  }


}

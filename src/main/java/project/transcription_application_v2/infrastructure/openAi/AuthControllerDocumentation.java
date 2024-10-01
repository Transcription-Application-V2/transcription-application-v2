package project.transcription_application_v2.infrastructure.openAi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import project.transcription_application_v2.infrastructure.exceptions.throwable.BadRequestException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.ForbiddenException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.security.dto.AuthenticationRequest;
import project.transcription_application_v2.infrastructure.security.dto.AuthenticationResponse;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;
import project.transcription_application_v2.infrastructure.security.dto.RefreshTokenRequest;

public interface AuthControllerDocumentation {

  @Operation(summary = "Authenticate user", description = "Authenticate user with username and password",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
              schema = @Schema(implementation = AuthenticationRequest.class),
              examples = {
                  @ExampleObject(
                      name = "AdminUser",
                      summary = "Admin user example",
                      value = "{\"username\": \"admin\", \"password\": \"password\"}"
                  ),
                  @ExampleObject(
                      name = "RegularUser",
                      summary = "Regular user example",
                      value = "{\"username\": \"user1\", \"password\": \"password\"}"
                  )
              }
          )
      )
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User authenticated successfully",
          content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid credentials",
          content = @Content(schema = @Schema(implementation = BadRequestException.class)))
  })
  ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest authenticationRequest)
      throws BadRequestException;

  @Operation(summary = "Refresh token", description = "Refresh the authentication token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
          content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid refresh token",
          content = @Content(schema = @Schema(implementation = ForbiddenException.class))),
      @ApiResponse(responseCode = "404", description = "Refresh token not found",
          content = @Content(schema = @Schema(implementation = NotFoundException.class)))
  })
  ResponseEntity<AuthenticationResponse> refreshToken(
      @RequestBody RefreshTokenRequest refreshTokenRequest)
      throws ForbiddenException, NotFoundException;

  @Operation(summary = "Invalidate session", description = "Invalidate the current user session")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Session invalidated successfully",
          content = @Content(schema = @Schema(implementation = MessageResponse.class)))
  })
  ResponseEntity<MessageResponse> invalidate();
}
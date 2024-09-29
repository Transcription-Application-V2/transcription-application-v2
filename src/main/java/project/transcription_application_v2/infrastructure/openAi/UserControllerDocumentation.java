package project.transcription_application_v2.web.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.security.dto.CreateUserRequest;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;

public interface UserControllerDocumentation {

    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Username or email already exists",
            content = @Content(mediaType = "application/json"))
    })
    ResponseEntity<MessageResponse> handleSignUp(@RequestBody CreateUserRequest createUserRequest) throws BadResponseException;
}
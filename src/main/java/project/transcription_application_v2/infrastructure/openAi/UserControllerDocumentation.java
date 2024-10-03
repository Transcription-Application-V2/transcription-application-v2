package project.transcription_application_v2.infrastructure.openAi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.transcription_application_v2.domain.user.dto.UpdateUser;
import project.transcription_application_v2.domain.user.dto.UserView;
import project.transcription_application_v2.infrastructure.exceptions.throwable.BadRequestException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.security.dto.CreateUserRequest;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;

public interface UserControllerDocumentation {

    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    })
    @PostMapping("/create")
    ResponseEntity<MessageResponse> handleSignUp(
        @RequestBody @Valid CreateUserRequest createUserRequest
    ) throws BadRequestException;

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserView.class))),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden",
            content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<UserView> get(@PathVariable Long id) throws NotFoundException;

    @Operation(summary = "Delete user by ID", description = "Deletes a user by their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden",
            content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<Void> delete(@PathVariable Long id)
        throws NotFoundException, BadRequestException;

    @Operation(summary = "Update user by ID", description = "Updates a user by their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserView.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden",
            content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<UserView> update(
        @PathVariable Long id,
        @RequestBody @Valid UpdateUser dto
    ) throws NotFoundException, BadRequestException;
}
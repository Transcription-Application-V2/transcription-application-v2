package project.transcription_application_v2.infrastructure.openAi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import project.transcription_application_v2.domain.paragraph.dto.ParagraphView;
import project.transcription_application_v2.domain.paragraph.dto.UpdateParagraph;
import project.transcription_application_v2.domain.paragraph.dto.UpdateSpeakers;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;

public interface ParagraphControllerDocumentation {

  @Operation(summary = "Get paragraph by ID", description = "Retrieves a paragraph by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Paragraph retrieved successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParagraphView.class))),
      @ApiResponse(responseCode = "404", description = "Paragraph not found"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<ParagraphView> get(@PathVariable Long id) throws NotFoundException;

  @Operation(summary = "Update paragraph by ID", description = "Updates a paragraph by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Paragraph updated successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParagraphView.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidException.class))),
      @ApiResponse(responseCode = "404", description = "Paragraph not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
      @ApiResponse(responseCode = "403", description = "Forbidden",
          content = @Content(mediaType = "application/json"))
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<ParagraphView> update(
      @PathVariable Long id,
      @RequestBody @Valid UpdateParagraph dto
  ) throws NotFoundException;

  @Operation(summary = "Delete paragraph by ID", description = "Deletes a paragraph by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Paragraph deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Paragraph not found"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<Void> delete(@PathVariable Long id) throws NotFoundException;

  @Operation(summary = "Update all speakers", description = "Updates all speakers in a paragraph.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Speakers updated successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidException.class))),
      @ApiResponse(responseCode = "404", description = "Paragraph not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class))),
      @ApiResponse(responseCode = "403", description = "Forbidden",
          content = @Content(mediaType = "application/json"))
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<Void> updateAllSpeakers(
      @RequestBody @Valid UpdateSpeakers dto
  ) throws NotFoundException;
}
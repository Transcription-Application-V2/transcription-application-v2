package project.transcription_application_v2.infrastructure.openAi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file.dto.DeletedFilesResponse;
import project.transcription_application_v2.domain.file.dto.FileView;
import project.transcription_application_v2.domain.file.dto.UploadedFilesResponse;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.exceptions.NotFoundException;

public interface FileControllerDocumentation {

  @Operation(summary = "Upload files", description = "Upload multiple files")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Files uploaded successfully",
          content = @Content(schema = @Schema(implementation = UploadedFilesResponse.class))),
      @ApiResponse(responseCode = "400", description = "No files provided",
          content = @Content(schema = @Schema(implementation = BadResponseException.class)))
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<UploadedFilesResponse> upload(@RequestParam("files") List<MultipartFile> files)
      throws BadResponseException;

  @Operation(summary = "Delete files", description = "Delete files by their IDs")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Files deleted successfully",
          content = @Content(schema = @Schema(implementation = DeletedFilesResponse.class))),
      @ApiResponse(responseCode = "400", description = "No IDs provided",
          content = @Content(schema = @Schema(implementation = BadResponseException.class)))
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<DeletedFilesResponse> delete(@RequestParam("ids") List<Long> ids)
      throws BadResponseException, NotFoundException;

  @Operation(summary = "Get all files", description = "Get all files with pagination")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Files retrieved successfully",
          content = @Content(schema = @Schema(implementation = Page.class)))
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<Page<FileView>> getAll(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size);

  @Operation(summary = "Get current user's files", description = "Get files of the current user with pagination")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Files retrieved successfully",
          content = @Content(schema = @Schema(implementation = Page.class)))
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<Page<FileView>> getCurrentUsers(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size);
}
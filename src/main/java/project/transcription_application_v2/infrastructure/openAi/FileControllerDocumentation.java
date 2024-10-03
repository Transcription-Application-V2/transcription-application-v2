package project.transcription_application_v2.infrastructure.openAi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file.annotations.not_empty_file_list.NotEmptyFileList;
import project.transcription_application_v2.domain.file.annotations.not_empty_list_ids.NotEmptyIdList;
import project.transcription_application_v2.domain.file.dto.DeletedFilesResponse;
import project.transcription_application_v2.domain.file.dto.FileView;
import project.transcription_application_v2.domain.file.dto.UploadedFilesResponse;
import project.transcription_application_v2.infrastructure.exceptions.throwable.BadRequestException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;

public interface FileControllerDocumentation {

  @Operation(summary = "Upload files", description = "Upload multiple files")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Files uploaded successfully",
          content = @Content(schema = @Schema(implementation = UploadedFilesResponse.class))),
      @ApiResponse(responseCode = "400", description = "No files provided",
          content = @Content(schema = @Schema(implementation = BadRequestException.class)))
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<UploadedFilesResponse> upload(
      @Valid @NotEmptyFileList @RequestParam("files") List<MultipartFile> files
  ) throws BadRequestException;

  @Operation(summary = "Delete files", description = "Delete files by their IDs")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Files deleted successfully",
          content = @Content(schema = @Schema(implementation = DeletedFilesResponse.class))),
      @ApiResponse(responseCode = "400", description = "No IDs provided",
          content = @Content(schema = @Schema(implementation = BadRequestException.class)))
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<DeletedFilesResponse> delete(
      @Valid @NotEmptyIdList @RequestParam("ids") List<Long> ids
  ) throws BadRequestException, NotFoundException;

  @Operation(summary = "Get all files", description = "Get all files with pagination")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Files retrieved successfully",
          content = @Content(schema = @Schema(implementation = Page.class)))
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<Page<FileView>> getAll(@RequestParam(required = false) String group,
      Pageable pageable);

  @Operation(summary = "Get current user's files", description = "Get files of the current user with pagination")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Files retrieved successfully",
          content = @Content(schema = @Schema(implementation = Page.class)))
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<Page<FileView>> getCurrentUsers(@RequestParam(required = false) String group,
      Pageable pageable);

  @Operation(summary = "Get file by ID", description = "Get a file by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "File retrieved successfully",
          content = @Content(schema = @Schema(implementation = FileView.class))),
      @ApiResponse(responseCode = "404", description = "File not found",
          content = @Content(schema = @Schema(implementation = NotFoundException.class)))
  })
  @SecurityRequirement(name = "bearerAuth")
  ResponseEntity<FileView> getById(@PathVariable Long id) throws NotFoundException;
}
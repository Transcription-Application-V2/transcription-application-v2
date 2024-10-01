package project.transcription_application_v2.web;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file.annotations.not_empty_file_list.NotEmptyFileList;
import project.transcription_application_v2.domain.file.annotations.not_empty_list_ids.NotEmptyIdList;
import project.transcription_application_v2.domain.file.dto.DeletedFilesResponse;
import project.transcription_application_v2.domain.file.dto.FileView;
import project.transcription_application_v2.domain.file.dto.UploadedFilesResponse;
import project.transcription_application_v2.domain.file.service.FileService;
import project.transcription_application_v2.infrastructure.exceptions.throwable.BadRequestException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.openAi.FileControllerDocumentation;

@RestController
@RequestMapping("/api/v2/file")
@RequiredArgsConstructor
public class FileController implements FileControllerDocumentation {

  private final FileService service;

  @PostMapping(path = "/upload", consumes = {"multipart/form-data"}, produces = "application/json")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<UploadedFilesResponse> upload(
      @Valid @NotEmptyFileList @RequestParam("files") List<MultipartFile> files
  ) throws BadRequestException {

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(service.create(files));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN') || @filePermissionEvaluator.ownerUserAccess(authentication, #id)")
  public ResponseEntity<FileView> getById(@PathVariable Long id) throws NotFoundException {
    return ResponseEntity
        .ok()
        .body(service.getById(id));
  }

  @DeleteMapping
  @PreAuthorize("hasAnyRole('ADMIN') || @filePermissionEvaluator.ownerUserAccess(authentication, #ids)")
  public ResponseEntity<DeletedFilesResponse> delete(
      @Valid @NotEmptyIdList @RequestParam("ids") List<Long> ids
  ) throws BadRequestException, NotFoundException {

    return ResponseEntity
        .ok()
        .body(service.delete(ids));
  }

  @GetMapping("/all")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<Page<FileView>> getAll(Pageable pageable) {

    return ResponseEntity
        .ok()
        .body(service.getAll(pageable));
  }

  @GetMapping("/current-user")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<Page<FileView>> getCurrentUsers(Pageable pageable) {

    return ResponseEntity
        .ok()
        .body(service.getCurrentUsers(pageable));
  }

}

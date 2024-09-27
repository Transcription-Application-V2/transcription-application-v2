package project.transcription_application_v2.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file.dto.DeletedFilesResponse;
import project.transcription_application_v2.domain.file.dto.FileView;
import project.transcription_application_v2.domain.file.dto.UploadedFilesResponse;
import project.transcription_application_v2.domain.file.service.FileProcessingService;
import project.transcription_application_v2.domain.file.service.FileService;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;

import java.util.List;

@RestController
@RequestMapping("/api/v2/file")
@RequiredArgsConstructor
public class FileController {

  private final FileProcessingService fileProcessingService;
  private final FileService fileService;

  @PostMapping(path = "/upload", consumes = {"multipart/form-data"}, produces = "application/json")
  public ResponseEntity<UploadedFilesResponse> upload(@RequestParam("files") List<MultipartFile> files) throws BadResponseException {

    if (files.isEmpty())
      throw new BadResponseException("No files provided: {}");

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(fileProcessingService.process(files));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<DeletedFilesResponse> delete(@RequestParam("ids") List<Long> ids) throws BadResponseException {

    if (ids.isEmpty())
      throw new BadResponseException("No ids provided: {}");

    return ResponseEntity
        .ok()
        .body(fileProcessingService.delete(ids));
  }

  @GetMapping("/all")
  public ResponseEntity<Page<FileView>> getAll(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    Pageable pageable = PageRequest.of(page, size);

    return ResponseEntity
        .ok()
        .body(fileService.getAll(pageable));
  }

  @GetMapping("/current-user")
  public ResponseEntity<Page<FileView>> getCurrentUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    Pageable pageable = PageRequest.of(page, size);

    return ResponseEntity
        .ok()
        .body(fileService.getCurrentUsers(pageable));
  }

}

package project.transcription_application_v2.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file.dto.UploadedFilesResponse;
import project.transcription_application_v2.domain.file.service.FileProcessingService;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;

import java.util.List;

@RestController
@RequestMapping("/api/v2/file")
@RequiredArgsConstructor
public class FileController {

  private final FileProcessingService fileProcessingService;

  @PostMapping(path = "/upload", consumes = {"multipart/form-data"}, produces = "application/json")
  public ResponseEntity<UploadedFilesResponse> uploadFile(@RequestParam("files") List<MultipartFile> files) throws BadResponseException {

    if (files.isEmpty())
      throw new BadResponseException("No files provided: {}");

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(fileProcessingService.processFiles(files));

  }
}

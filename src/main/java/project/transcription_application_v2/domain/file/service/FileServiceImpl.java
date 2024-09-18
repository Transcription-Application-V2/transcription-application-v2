package project.transcription_application_v2.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file.dto.UploadedFilesResponse;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.file.repository.FileRepository;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;
import project.transcription_application_v2.domain.transcription.entity.Transcription;
import project.transcription_application_v2.domain.user.service.UserService;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

  private final FileRepository fileRepository;

  private final UserService userService;

  public File create(FileMeta fileMeta, Transcription transcription) {
    File file = new File(userService.getLoggedUser(), fileMeta, transcription);
    fileMeta.setFile(file);
    transcription.setFile(file);
    return fileRepository.save(file);
  }
}

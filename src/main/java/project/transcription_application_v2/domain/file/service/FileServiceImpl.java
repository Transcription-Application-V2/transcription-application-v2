package project.transcription_application_v2.domain.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transcription_application_v2.domain.file.dto.FileView;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.file.repository.FileRepository;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;
import project.transcription_application_v2.domain.file_meta.service.FileMetaService;
import project.transcription_application_v2.domain.transcription.entity.Transcription;
import project.transcription_application_v2.domain.transcription.service.TranscriptionService;
import project.transcription_application_v2.domain.user.service.UserService;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.mappers.FileMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

  private final FileRepository fileRepository;
  private final UserService userService;

  private final FileMapper fileMapper;

  public File create(FileMeta fileMeta, Transcription transcription) {
    File file = new File(userService.getLoggedUser(), fileMeta, transcription);
    fileMeta.setFile(file);
    transcription.setFile(file);
    return fileRepository.save(file);
  }

  public File get(Long id) throws BadResponseException {
    return getFileById(id);
  }

  @Transactional
  public void delete(File file) throws BadResponseException {
    fileRepository.delete(getFileById(file.getId()));
  }

  public Page<FileView> getAll(Pageable pageable) {
    return fileRepository.retrieveAllFiles(pageable)
        .map(fileMapper::toView);
  }

  public Page<FileView> getCurrentUsers(Pageable pageable) {
    return fileRepository.retrieveAllFilesByUserId(userService.getLoggedUser().getId(), pageable)
        .map(fileMapper::toView);
  }

  private File getFileById(Long id) throws BadResponseException {
    return fileRepository.findById(id)
        .orElseThrow(() -> new BadResponseException("File not found"));

  }
}

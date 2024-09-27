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
import project.transcription_application_v2.domain.file_meta.dto.FileMetaView;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;
import project.transcription_application_v2.domain.file_meta.service.FileMetaService;
import project.transcription_application_v2.domain.transcription.dto.TranscriptionView;
import project.transcription_application_v2.domain.transcription.entity.Transcription;
import project.transcription_application_v2.domain.transcription.service.TranscriptionService;
import project.transcription_application_v2.domain.user.service.UserService;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.mappers.FileMetaMapper;
import project.transcription_application_v2.infrastructure.mappers.TranscriptionMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

  private final FileRepository fileRepository;
  private final UserService userService;
  private final FileMetaService fileMetaService;
  private final TranscriptionService transcriptionService;

  private final FileMetaMapper fileMetaMapper;
  private final TranscriptionMapper transcriptionMapper;

  public File create(FileMeta fileMeta, Transcription transcription) {
    File file = new File(userService.getLoggedUser(), fileMeta, transcription);
    fileMeta.setFile(file);
    transcription.setFile(file);
    return fileRepository.save(file);
  }

  public File get(Long id) throws BadResponseException {
    Optional<File> file = fileRepository.findById(id);

    if (file.isPresent())
      return file.get();

    throw new BadResponseException("File not found");
  }

  @Transactional
  public void delete(File file) {
    fileRepository.deleteFileMetaByFileId(file.getId());

    fileRepository.deleteTranscriptionByFileId(file.getId());

    fileRepository.deleteFileById(file.getId());
  }

  public Page<FileView> getAll(Pageable pageable) {
    return mapFilesToFilesViews(fileRepository.retrieveAllFiles(pageable));
  }

  public Page<FileView> getCurrentUsers(Pageable pageable) {
    return mapFilesToFilesViews(fileRepository.retrieveAllFilesByUserId(userService.getLoggedUser().getId(), pageable));
  }

  private Page<FileView> mapFilesToFilesViews(Page<File> files) {
    return files
        .map(file -> {
          FileMeta fileMeta = fileMetaService.findByFileId(file.getId());
          Transcription transcription = transcriptionService.findByFileId(file.getId());

          FileMetaView fileMetaView = fileMetaMapper.toFileMetaView(fileMeta);
          TranscriptionView transcriptionView = transcriptionMapper.toTranscriptionView(transcription);

          return new FileView(file.getId(), fileMetaView, transcriptionView);
        });
  }

}

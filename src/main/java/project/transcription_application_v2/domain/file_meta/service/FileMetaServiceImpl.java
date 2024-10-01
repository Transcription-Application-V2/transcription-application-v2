package project.transcription_application_v2.domain.file_meta.service;

import static project.transcription_application_v2.infrastructure.exceptions.ExceptionMessages.FILE_META_NOT_FOUND;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.file.service.FileService;
import project.transcription_application_v2.domain.file_meta.dto.CreateFileMeta;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;
import project.transcription_application_v2.domain.file_meta.repository.FileMetaRepository;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.mappers.FileMetaMapper;

@Service
@RequiredArgsConstructor
public class FileMetaServiceImpl implements FileMetaService {

  private FileService fileService;

  private final FileMetaRepository fileMetaRepository;

  private final FileMetaMapper fileMetaMapper;

  @Autowired
  public void setFileService(@Lazy FileService fileService) {
    this.fileService = fileService;
  }

  public void create(CreateFileMeta dto) throws NotFoundException {
    File file = fileService.findById(dto.fileId());

    fileMetaRepository.save(fileMetaMapper.toEntity(dto, file));
  }

  @Override
  public void delete(Long fileId) throws NotFoundException {
    FileMeta byId = findById(fileId);

    fileMetaRepository.delete(byId);
  }

  @Override
  public FileMeta findById(Long fileId) throws NotFoundException {
    return fileMetaRepository.findById(fileId)
        .orElseThrow(() -> new NotFoundException(FILE_META_NOT_FOUND, fileId));
  }

  @Override
  public boolean moreThenOneDropboxDownloadUrls(String downloadUrl) {
    return fileMetaRepository.countAllByDownloadUrl(downloadUrl) > 1;
  }

  @Override
  public boolean moreThenOneAssemblyAiIds(String assemblyAiId) {
    return fileMetaRepository.countAllByAssemblyAiId(assemblyAiId) > 1;
  }

  @Override
  public Optional<FileMeta> findFirstByDownloadUrl(String downloadUrl) {
    return fileMetaRepository.findFirstByDownloadUrl(downloadUrl);
  }

}

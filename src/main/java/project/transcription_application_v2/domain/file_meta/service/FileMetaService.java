package project.transcription_application_v2.domain.file_meta.service;

import java.util.Optional;
import project.transcription_application_v2.domain.file_meta.dto.CreateFileMeta;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;
import project.transcription_application_v2.infrastructure.exceptions.throwable.BadRequestException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;

public interface FileMetaService {

  void create(CreateFileMeta dto) throws BadRequestException, NotFoundException;

  void delete(Long fileId) throws NotFoundException;

  FileMeta findById(Long fileId) throws NotFoundException;

  boolean moreThenOneDropboxDownloadUrls(String downloadUrl);

  boolean moreThenOneAssemblyAiIds(String assemblyAiId);

  Optional<FileMeta> findFirstByDownloadUrl(String downloadUrl);

}

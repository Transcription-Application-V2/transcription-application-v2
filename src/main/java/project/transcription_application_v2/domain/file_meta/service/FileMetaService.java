package project.transcription_application_v2.domain.file_meta.service;

import project.transcription_application_v2.domain.file_meta.dto.CreateFileMeta;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.exceptions.NotFoundException;

public interface FileMetaService {

  void create(CreateFileMeta dto) throws BadResponseException, NotFoundException;

  void delete(Long fileId) throws NotFoundException;

  FileMeta findById(Long fileId) throws NotFoundException;
}

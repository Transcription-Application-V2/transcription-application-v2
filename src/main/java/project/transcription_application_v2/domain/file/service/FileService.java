package project.transcription_application_v2.domain.file.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.transcription_application_v2.domain.file.dto.FileView;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;
import project.transcription_application_v2.domain.transcription.entity.Transcription;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;

import java.util.List;

public interface FileService {
  File create (FileMeta fileMeta, Transcription transcription);

  File get(Long id) throws BadResponseException;

  void delete(File file);

  Page<FileView> getAll(Pageable pageable);

  Page<FileView> getCurrentUsers(Pageable pageable);
}

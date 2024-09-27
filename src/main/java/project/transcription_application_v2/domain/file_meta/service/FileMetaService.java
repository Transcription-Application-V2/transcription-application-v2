package project.transcription_application_v2.domain.file_meta.service;

import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;

public interface FileMetaService {

  FileMeta create(MultipartFile file, String downloadUrl, String assemblyId);

  FileMeta findByFileId(Long fileId);
}

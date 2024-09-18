package project.transcription_application_v2.domain.file_meta.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;
import project.transcription_application_v2.domain.file_meta.repository.FileMetaRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileMetaServiceImpl implements FileMetaService {

  private final FileMetaRepository fileMetaRepository;

  public FileMeta create(MultipartFile file, String downloadUrl, String assemblyId) {
    return new FileMeta(
        file.getName(),
        file.getSize(),
        file.getContentType(),
        LocalDateTime.now(),
        downloadUrl,
        assemblyId,
        null);
  }


}

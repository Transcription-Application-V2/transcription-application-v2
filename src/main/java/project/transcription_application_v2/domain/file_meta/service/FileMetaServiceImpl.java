package project.transcription_application_v2.domain.file_meta.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;
import project.transcription_application_v2.domain.file_meta.repository.FileMetaRepository;
import project.transcription_application_v2.infrastructure.mappers.FileMetaMapper;

@Service
@RequiredArgsConstructor
public class FileMetaServiceImpl implements FileMetaService {

  private final FileMetaRepository fileMetaRepository;

  private final FileMetaMapper fileMetaMapper;

  //TODO:: make it from here to save into the database
  public FileMeta create(MultipartFile file, String downloadUrl, String assemblyId) {
    return fileMetaMapper.toEntity(file, downloadUrl, assemblyId);
  }

  public FileMeta findByFileId(Long fileId) {
    Optional<FileMeta> fileMeta = fileMetaRepository.findByFileId(fileId);
    return fileMeta.orElse(null);
  }

}

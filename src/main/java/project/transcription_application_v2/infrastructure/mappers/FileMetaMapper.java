package project.transcription_application_v2.infrastructure.mappers;

import org.springframework.stereotype.Component;
import project.transcription_application_v2.domain.file_meta.dto.FileMetaView;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;

@Component
public class FileMetaMapper {

  public FileMetaView toFileMetaView(FileMeta fileMeta){
    return new FileMetaView(
        fileMeta.getId(),
        fileMeta.getName(),
        fileMeta.getSize(),
        fileMeta.getType(),
        fileMeta.getDate(),
        fileMeta.getDownloadUrl(),
        fileMeta.getAssemblyAiId());
  }

}

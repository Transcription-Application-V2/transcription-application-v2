package project.transcription_application_v2.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.transcription_application_v2.domain.file.dto.FileView;
import project.transcription_application_v2.domain.file.entity.File;

@Mapper(
    componentModel = "spring",
    uses = {FileMetaMapper.class , TranscriptionMapper.class},
    implementationName = "FileMapperImpl"
)
public interface FileMapper {

  @Mapping(target = "fileMetaView", source = "fileMeta")
  @Mapping(target = "transcriptionView", source = "transcription")
  FileView toView(File entity);
}

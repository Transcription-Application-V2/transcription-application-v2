package project.transcription_application_v2.infrastructure.mappers;

import org.mapstruct.Mapper;
import project.transcription_application_v2.domain.file_meta.dto.FileMetaView;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;

@Mapper(
    componentModel = "spring",
    implementationName = "FileMetaMapperImpl"
)
public interface FileMetaMapper {

  FileMetaView toView(FileMeta entity);
}

package project.transcription_application_v2.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.file_meta.dto.CreateFileMeta;
import project.transcription_application_v2.domain.file_meta.dto.FileMetaView;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;

@Mapper(
    componentModel = "spring",
    implementationName = "FileMetaMapperImpl"
)
public interface FileMetaMapper {

  FileMetaView toView(FileMeta entity);

  @Mappings({
      @Mapping(target = "name", source = "dto.name"),
      @Mapping(target = "size", source = "dto.file.size"),
      @Mapping(target = "type", source = "dto.file.contentType"),
      @Mapping(target = "downloadUrl", source = "dto.downloadUrl"),
      @Mapping(target = "assemblyAiId", source = "dto.assemblyId"),
      @Mapping(target = "file", source = "file")
  })
  FileMeta toEntity(CreateFileMeta dto, File file);
}

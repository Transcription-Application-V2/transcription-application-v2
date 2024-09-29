package project.transcription_application_v2.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file_meta.dto.FileMetaView;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;

@Mapper(
    componentModel = "spring",
    implementationName = "FileMetaMapperImpl"
)
public interface FileMetaMapper {

  FileMetaView toView(FileMeta entity);

  @Mappings({
      @Mapping(target = "name", source = "file.originalFilename"),
      @Mapping(target = "size", source = "file.size"),
      @Mapping(target = "type", source = "file.contentType"),
      @Mapping(target = "downloadUrl", source = "downloadUrl"),
      @Mapping(target = "assemblyAiId", source = "assemblyId")
  })
  FileMeta toEntity(MultipartFile file, String downloadUrl, String assemblyId);
}

package project.transcription_application_v2.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import project.transcription_application_v2.domain.transcription.dto.CreateTranscription;
import project.transcription_application_v2.domain.transcription.dto.TranscriptionView;
import project.transcription_application_v2.domain.transcription.entity.Transcription;

@Mapper(
    componentModel = "spring",
    uses = {ParagraphMapper.class},
    implementationName = "TranscriptionMapperImpl"
)
public interface TranscriptionMapper {

  TranscriptionView toView(Transcription entity);

  @Mappings({
      @Mapping(target = "name", source = "dto.name"),
      @Mapping(target = "size", expression = "java(dto.transcript().getText().map(String::length).map(Long::valueOf).orElse(0L))"),
  })
  Transcription toEntity(CreateTranscription dto);
}

package project.transcription_application_v2.infrastructure.mappers;

import com.assemblyai.api.resources.transcripts.types.Transcript;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;
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
      @Mapping(target = "name", source = "name"),
      @Mapping(target = "size", expression = "java(transcript.getText().map(String::length).map(Long::valueOf).orElse(0L))"),
      @Mapping(target = "paragraphs", source = "paragraphs")
  })
  Transcription toEntity(String name, List<Paragraph> paragraphs, Transcript transcript);
}

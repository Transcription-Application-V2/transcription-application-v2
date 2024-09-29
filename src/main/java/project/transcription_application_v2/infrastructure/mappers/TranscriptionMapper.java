package project.transcription_application_v2.infrastructure.mappers;

import org.mapstruct.Mapper;
import project.transcription_application_v2.domain.transcription.dto.TranscriptionView;
import project.transcription_application_v2.domain.transcription.entity.Transcription;

@Mapper(
    componentModel = "spring",
    uses = {ParagraphMapper.class},
    implementationName = "TranscriptionMapperImpl"
)
public interface TranscriptionMapper {

  TranscriptionView toView(Transcription entity);
}

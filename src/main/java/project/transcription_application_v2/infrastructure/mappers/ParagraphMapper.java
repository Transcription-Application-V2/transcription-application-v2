package project.transcription_application_v2.infrastructure.mappers;

import org.mapstruct.Mapper;
import project.transcription_application_v2.domain.paragraph.dto.ParagraphView;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;

@Mapper(
    componentModel = "spring",
    implementationName = "ParagraphMapperImpl"
)
public interface ParagraphMapper {

  ParagraphView toView(Paragraph entity);
}

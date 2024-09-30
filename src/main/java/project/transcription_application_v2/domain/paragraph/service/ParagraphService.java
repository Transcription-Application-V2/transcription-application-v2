package project.transcription_application_v2.domain.paragraph.service;

import java.util.List;
import project.transcription_application_v2.domain.paragraph.dto.CreateParagraph;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;
import project.transcription_application_v2.infrastructure.exceptions.NotFoundException;

public interface ParagraphService {

  List<Paragraph> create(CreateParagraph dto) throws NotFoundException;
}

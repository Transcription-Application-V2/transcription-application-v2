package project.transcription_application_v2.domain.paragraph.service;

import project.transcription_application_v2.domain.paragraph.dto.CreateParagraph;
import project.transcription_application_v2.infrastructure.exceptions.NotFoundException;

public interface ParagraphService {

  void create(CreateParagraph dto) throws NotFoundException;

  void delete(Long paragraphId) throws NotFoundException;
}

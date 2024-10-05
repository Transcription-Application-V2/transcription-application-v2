package project.transcription_application_v2.domain.paragraph.service;

import project.transcription_application_v2.domain.paragraph.dto.CreateParagraph;
import project.transcription_application_v2.domain.paragraph.dto.ParagraphView;
import project.transcription_application_v2.domain.paragraph.dto.UpdateParagraph;
import project.transcription_application_v2.domain.paragraph.dto.UpdateSpeakers;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;

public interface ParagraphService {

  void create(CreateParagraph dto) throws NotFoundException;

  void delete(Long paragraphId) throws NotFoundException;

  ParagraphView update(Long id, UpdateParagraph dto) throws NotFoundException;

  ParagraphView get(Long id) throws NotFoundException;

  void updateSpeakers(UpdateSpeakers dto) throws NotFoundException;

  Paragraph findById(Long id) throws NotFoundException;
}

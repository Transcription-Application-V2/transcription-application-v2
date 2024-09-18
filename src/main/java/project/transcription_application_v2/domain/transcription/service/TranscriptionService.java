package project.transcription_application_v2.domain.transcription.service;

import project.transcription_application_v2.domain.paragraph.entity.Paragraph;
import project.transcription_application_v2.domain.transcription.entity.Transcription;

import java.util.List;

public interface TranscriptionService {
  Transcription create(String name, List<Paragraph> paragraphs, Long size);
}

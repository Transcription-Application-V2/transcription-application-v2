package project.transcription_application_v2.domain.paragraph.service;

import com.assemblyai.api.resources.transcripts.types.Transcript;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;

import java.util.List;

public interface ParagraphService {
  List<Paragraph> create(Transcript transcript);
}

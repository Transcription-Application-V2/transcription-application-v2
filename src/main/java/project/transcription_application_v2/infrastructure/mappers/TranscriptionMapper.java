package project.transcription_application_v2.infrastructure.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.transcription_application_v2.domain.transcription.dto.TranscriptionView;
import project.transcription_application_v2.domain.transcription.entity.Transcription;

@Component
@RequiredArgsConstructor
public class TranscriptionMapper {

  private final ParagraphMapper paragraphMapper;

  public TranscriptionView toTranscriptionView(Transcription transcription) {
    return new TranscriptionView(
        transcription.getId(),
        transcription.getName(),
        transcription.getSize(),
        transcription.getPublishedAt(),
        transcription.getLastModifiedAt(),
        paragraphMapper.toParagraphViewList(transcription.getParagraphs()));
  }

}

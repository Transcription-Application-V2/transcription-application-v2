package project.transcription_application_v2.domain.paragraph.service;

import static project.transcription_application_v2.infrastructure.exceptions.ExceptionMessages.PARAGRAPH_NOT_FOUND;

import com.assemblyai.api.resources.transcripts.types.TranscriptUtterance;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import project.transcription_application_v2.domain.paragraph.dto.CreateParagraph;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;
import project.transcription_application_v2.domain.paragraph.repository.ParagraphRepository;
import project.transcription_application_v2.domain.transcription.service.TranscriptionService;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParagraphServiceImpl implements ParagraphService {

  private TranscriptionService transcriptionService;

  private final ParagraphRepository paragraphRepository;

  @Autowired
  public void setTranscriptionService(@Lazy TranscriptionService transcriptionService) {
    this.transcriptionService = transcriptionService;
  }

  public void create(CreateParagraph dto) throws NotFoundException {

    List<Paragraph> paragraphs = new ArrayList<>();

    if (dto.transcript().getUtterances().isPresent())
      for (TranscriptUtterance utterance : dto.transcript().getUtterances().get()) {
        paragraphs.add(
            Paragraph.builder()
                .speaker(utterance.getSpeaker())
                .time((long) utterance.getStart())
                .text(utterance.getText())
                .transcription(transcriptionService.findById(dto.transcriptionId()))
                .build());
      }
    else
      log.warn("No utterances present..");

    paragraphRepository.saveAll(paragraphs);
  }

  @Override
  public void delete(Long paragraphId) throws NotFoundException {
    Paragraph byId = findById(paragraphId);

    paragraphRepository.delete(byId);
  }

  private Paragraph findById(Long paragraphId) throws NotFoundException {
    return paragraphRepository.findById(paragraphId)
        .orElseThrow(() -> new NotFoundException(PARAGRAPH_NOT_FOUND, paragraphId));
  }
}

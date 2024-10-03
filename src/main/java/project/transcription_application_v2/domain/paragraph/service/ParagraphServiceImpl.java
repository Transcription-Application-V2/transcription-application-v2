package project.transcription_application_v2.domain.paragraph.service;

import static project.transcription_application_v2.infrastructure.exceptions.ExceptionMessages.NO_PARAGRAPHS_FOUND;
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
import project.transcription_application_v2.domain.paragraph.dto.ParagraphView;
import project.transcription_application_v2.domain.paragraph.dto.UpdateParagraph;
import project.transcription_application_v2.domain.paragraph.dto.UpdateSpeakers;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;
import project.transcription_application_v2.domain.paragraph.repository.ParagraphRepository;
import project.transcription_application_v2.domain.transcription.service.TranscriptionService;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.mappers.ParagraphMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParagraphServiceImpl implements ParagraphService {

  private TranscriptionService transcriptionService;

  private final ParagraphRepository paragraphRepository;

  private final ParagraphMapper paragraphMapper;

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

  @Override
  public ParagraphView update(Long id, UpdateParagraph dto) throws NotFoundException {
    Paragraph paragraphToSave = findById(id);

    paragraphMapper.updateEntity(paragraphToSave, dto);

    return paragraphMapper.toView(paragraphRepository.save(paragraphToSave));
  }

  @Override
  public ParagraphView get(Long id) throws NotFoundException {
    return paragraphMapper.toView(findById(id));
  }

  @Override
  public void updateSpeakers(UpdateSpeakers dto) throws NotFoundException {
    if (!paragraphRepository.existsParagraphsByTranscription_Id(dto.fileId())) {
      throw new NotFoundException(NO_PARAGRAPHS_FOUND, dto.fileId());
    }

    List<Paragraph> allParagraphs = paragraphRepository.findAllByTranscription_Id(dto.fileId());

    for (Paragraph paragraph : allParagraphs) {
      if (paragraph.getSpeaker().equals(dto.oldSpeaker())) {
        paragraph.setSpeaker(dto.newSpeaker());
      }
    }

    paragraphRepository.saveAll(allParagraphs);
  }

  @Override
  public Paragraph findById(Long paragraphId) throws NotFoundException {
    return paragraphRepository.findById(paragraphId)
        .orElseThrow(() -> new NotFoundException(PARAGRAPH_NOT_FOUND, paragraphId));
  }
}

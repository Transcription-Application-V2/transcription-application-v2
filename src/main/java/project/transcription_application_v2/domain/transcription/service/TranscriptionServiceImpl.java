package project.transcription_application_v2.domain.transcription.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.transcription_application_v2.domain.paragraph.dto.CreateParagraph;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;
import project.transcription_application_v2.domain.paragraph.service.ParagraphService;
import project.transcription_application_v2.domain.transcription.dto.CreateTranscription;
import project.transcription_application_v2.domain.transcription.entity.Transcription;
import project.transcription_application_v2.domain.transcription.repository.TranscriptionRepository;
import project.transcription_application_v2.infrastructure.exceptions.NotFoundException;
import project.transcription_application_v2.infrastructure.mappers.TranscriptionMapper;

@Service
@RequiredArgsConstructor
public class TranscriptionServiceImpl implements TranscriptionService {

  private final ParagraphService paragraphService;

  private final TranscriptionRepository transcriptionRepository;

  private final TranscriptionMapper transcriptionMapper;

  public Transcription create(CreateTranscription dto) throws NotFoundException {
    Transcription transcription = transcriptionRepository.save(transcriptionMapper.toEntity(dto));

    CreateParagraph createParagraph = new CreateParagraph(
        dto.transcript(),
        transcription.getId()
    );
    List<Paragraph> paragraphs = paragraphService.create(createParagraph);
    transcription.getParagraphs().addAll(paragraphs);

    return transcriptionRepository.save(transcription);
  }

  public Transcription findById(Long id) throws NotFoundException {
    return transcriptionRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Transcription not found"));
  }
}

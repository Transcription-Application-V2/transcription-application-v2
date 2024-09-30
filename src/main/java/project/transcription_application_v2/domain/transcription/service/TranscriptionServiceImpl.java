package project.transcription_application_v2.domain.transcription.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.file.service.FileService;
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
  private FileService fileService;

  private final TranscriptionRepository transcriptionRepository;

  private final TranscriptionMapper transcriptionMapper;

  @Autowired
  public void setFileService(@Lazy FileService fileService) {
    this.fileService = fileService;
  }

  public void create(CreateTranscription dto) throws NotFoundException {
    File file = fileService.findById(dto.fileId());

    Transcription transcription = transcriptionRepository.save(
        transcriptionMapper.toEntity(dto, file)
    );

    CreateParagraph createParagraph = new CreateParagraph(
        dto.transcript(),
        transcription.getId()
    );
    paragraphService.create(createParagraph);
  }

  public Transcription findById(Long id) throws NotFoundException {
    return transcriptionRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Transcription not found"));
  }

  @Override
  @Transactional
  public void delete(Long id) throws NotFoundException {
    Transcription toDelete = findById(id);

    for (Paragraph paragraph : toDelete.getParagraphs()) {
      paragraphService.delete(paragraph.getId());
    }

    transcriptionRepository.delete(toDelete);
  }
}

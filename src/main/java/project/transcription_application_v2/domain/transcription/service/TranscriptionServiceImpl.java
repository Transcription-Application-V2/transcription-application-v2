package project.transcription_application_v2.domain.transcription.service;

import com.assemblyai.api.resources.transcripts.types.Transcript;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;
import project.transcription_application_v2.domain.transcription.entity.Transcription;
import project.transcription_application_v2.domain.transcription.repository.TranscriptionRepository;
import project.transcription_application_v2.infrastructure.mappers.TranscriptionMapper;

@Service
@RequiredArgsConstructor
public class TranscriptionServiceImpl implements TranscriptionService {

  private final TranscriptionRepository transcriptionRepository;

  private final TranscriptionMapper transcriptionMapper;

  //TODO:: make it here to save the transcription into the db
  public Transcription create(String name, List<Paragraph> paragraphs, Transcript transcript) {
    return transcriptionMapper.toEntity(name, paragraphs, transcript);
  }

  public Transcription findByFileId(Long fileId) {
    return transcriptionRepository.findByFileIdWithParagraphs(fileId)
        .orElse(null);
  }
}

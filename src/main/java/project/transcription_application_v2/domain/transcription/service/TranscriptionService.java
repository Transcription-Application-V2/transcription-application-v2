package project.transcription_application_v2.domain.transcription.service;

import project.transcription_application_v2.domain.transcription.dto.CreateTranscription;
import project.transcription_application_v2.domain.transcription.entity.Transcription;
import project.transcription_application_v2.infrastructure.exceptions.NotFoundException;

public interface TranscriptionService {

  void create(CreateTranscription dto) throws NotFoundException;

  Transcription findById(Long id) throws NotFoundException;

  void delete(Long id) throws NotFoundException;

}

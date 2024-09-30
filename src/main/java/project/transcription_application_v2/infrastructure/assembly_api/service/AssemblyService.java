package project.transcription_application_v2.infrastructure.assembly_api.service;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.Transcript;
import com.assemblyai.api.resources.transcripts.types.TranscriptOptionalParams;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;
import project.transcription_application_v2.domain.file_meta.service.FileMetaService;
import project.transcription_application_v2.infrastructure.assembly_api.dto.AssemblyConvertedFile;
import project.transcription_application_v2.infrastructure.exceptions.AssemblyAIException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssemblyService {

  @Value("${assembly_ai.api.key}")
  private String assemblyAiApiKey;

  private AssemblyAI assemblyAI;

  private final FileMetaService fileMetaService;

  @PostConstruct
  public void init() {
    assemblyAI = AssemblyAI.builder()
        .apiKey(assemblyAiApiKey)
        .build();
  }

  public AssemblyConvertedFile transcribe(String downloadUrl) throws AssemblyAIException {
    try {
      Optional<FileMeta> firstByDownloadUrl = fileMetaService.findFirstByDownloadUrl(downloadUrl);
      if (firstByDownloadUrl.isPresent()) {
        Transcript byId = getById(firstByDownloadUrl.get().getAssemblyAiId());
        log.info("Transcription with id {} already exists. Reuse it", byId.getId());
        return new AssemblyConvertedFile(byId.getId(), downloadUrl, byId);
      }
      // Builds the params for the request to the Assembly AI API
      var params = TranscriptOptionalParams
          .builder()
          .speakerLabels(true)
          .build();
      // Receives the transcription from the Assembly AI API request
      Transcript transcript = assemblyAI
          .transcripts()
          .transcribe(downloadUrl, params);

      log.info("Transcription created successfully");
      return new AssemblyConvertedFile(transcript.getId(), downloadUrl, transcript);
    } catch (Exception exception) {
      log.error("Error transcribing the file: {}", exception.getMessage());
      throw new AssemblyAIException("Error transcribing the file: {}" + exception.getMessage());
    }
  }

  public Transcript getById(String id) throws AssemblyAIException {
    try {
      return this.assemblyAI
          .transcripts()
          .get(id);
    } catch (Exception exception) {
      log.error("Error while fetching transcription: {}", exception.getMessage());
      throw new AssemblyAIException("Error while fetching transcription: {}" + exception.getMessage());
    }
  }

  public void deleteById(String id) throws AssemblyAIException {
    try {
      if (fileMetaService.moreThenOneAssemblyAiIds(id)) {
        log.info("Transcription with id {} is still in use", id);
        return;
      }
      this.assemblyAI
          .transcripts()
          .delete(id);
      log.info("Transcript deleted successfully");
    } catch (Exception exception) {
      log.error("Error deleting transcription: {}", exception.getMessage());
      throw new AssemblyAIException("Error deleting transcription: {}" + exception.getMessage());
    }
  }
}

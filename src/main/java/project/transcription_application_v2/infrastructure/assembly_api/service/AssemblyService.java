package project.transcription_application_v2.infrastructure.assembly_api.service;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.Transcript;
import com.assemblyai.api.resources.transcripts.types.TranscriptOptionalParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.transcription_application_v2.infrastructure.assembly_api.dto.AssemblyConvertedFile;
import project.transcription_application_v2.infrastructure.exceptions.AssemblyAIException;

import java.util.List;

@Service
public class AssemblyService {

  private final static Logger logger = LoggerFactory.getLogger(AssemblyService.class);

  @Value("${assembly_ai.api.key}")
  private String assemblyAiApiKey;

  private AssemblyAI assemblyAI;

  @PostConstruct
  public void init() {
    assemblyAI = AssemblyAI.builder()
        .apiKey(assemblyAiApiKey)
        .build();
  }

  public AssemblyConvertedFile transcribe(String downloadUrl) throws AssemblyAIException {
    try {
      // Builds the params for the request to the Assembly AI API
      var params = TranscriptOptionalParams
          .builder()
          .speakerLabels(true)
          .build();
      // Receives the transcription from the Assembly AI API request
      Transcript transcript = assemblyAI.transcripts().transcribe(downloadUrl, params);
      return new AssemblyConvertedFile(transcript.getId(), downloadUrl, transcript);
    } catch (Exception exception) {
      logger.error("Error transcribing the file: {}", exception.getMessage());
      throw new AssemblyAIException("Error transcribing the file: {}" + exception.getMessage());
    }
  }

  public Transcript getById(String id) throws AssemblyAIException {
    try {
      return this.assemblyAI.transcripts().get(id);
    } catch (Exception exception) {
      logger.error("Error while fetching transcription: {}", exception.getMessage());
      throw new AssemblyAIException("Error while fetching transcription: {}" + exception.getMessage());
    }
  }

  public void deleteById(String id) throws AssemblyAIException {
    try {
      this.assemblyAI.transcripts().delete(id);
    } catch (Exception exception) {
      logger.error("Error deleting transcription: {}", exception.getMessage());
      throw new AssemblyAIException("Error deleting transcription: {}" + exception.getMessage());
    }
  }
}

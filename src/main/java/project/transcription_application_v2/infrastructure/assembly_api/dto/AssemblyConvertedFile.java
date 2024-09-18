package project.transcription_application_v2.infrastructure.assembly_api.dto;

import com.assemblyai.api.resources.transcripts.types.Transcript;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssemblyConvertedFile {

  private String assemblyId;
  private String downloadUrl;
  private Transcript transcript;
}

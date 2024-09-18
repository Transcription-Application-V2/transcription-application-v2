package project.transcription_application_v2.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UploadedFilesResponse {

  private List<String> processedFiles;
  private List<String> unprocessedFiles;
}

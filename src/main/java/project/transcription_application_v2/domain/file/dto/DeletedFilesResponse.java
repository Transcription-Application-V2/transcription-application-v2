package project.transcription_application_v2.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeletedFilesResponse {

  private List<String> deletedFiles;
  private List<Long> failedIds;
}

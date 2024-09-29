package project.transcription_application_v2.domain.file.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletedFilesResponse {

  private List<String> deletedFiles;
  private List<Long> failedIds;
}

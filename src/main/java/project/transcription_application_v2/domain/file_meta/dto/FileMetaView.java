package project.transcription_application_v2.domain.file_meta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaView {

  private Long id;

  private String name;

  private Long size;

  private String type;

  private LocalDateTime date;

  private String downloadUrl;

  private String assemblyAiId;
}

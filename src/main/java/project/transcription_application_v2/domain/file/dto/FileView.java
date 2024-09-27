package project.transcription_application_v2.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.transcription_application_v2.domain.file_meta.dto.FileMetaView;
import project.transcription_application_v2.domain.transcription.dto.TranscriptionView;

@Data
@AllArgsConstructor
public class FileView {

  private Long id;

  private FileMetaView fileMetaView;

  private TranscriptionView transcriptionView;
}

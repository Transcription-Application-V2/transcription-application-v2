package project.transcription_application_v2.domain.transcription.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.transcription_application_v2.domain.paragraph.dto.ParagraphView;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptionView {

  private Long id;

  private String name;

  private Long size;

  private LocalDateTime publishedAt;

  private LocalDateTime lastModifiedAt;

  private List<ParagraphView> paragraphs;
}

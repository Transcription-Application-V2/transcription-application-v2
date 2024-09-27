package project.transcription_application_v2.domain.paragraph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParagraphView {

  private Long id;
  private String speaker;
  private Long time;
  private String text;
}

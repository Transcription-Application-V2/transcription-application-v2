package project.transcription_application_v2.domain.paragraph.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.transcription_application_v2.domain.BaseEntity;
import project.transcription_application_v2.domain.transcription.entity.Transcription;

@Entity
@Table(name = "paragraphs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Paragraph extends BaseEntity {

  @Column(nullable = false)
  private String speaker;

  @Column(nullable = false)
  private Long time;

  @Column(columnDefinition = "text")
  private String text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "transcription_id")
  private Transcription transcription;
}

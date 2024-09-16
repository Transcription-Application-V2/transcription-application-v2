package project.transcription_application_v2.domain.transcription.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.transcription_application_v2.domain.BaseEntity;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transcriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transcription extends BaseEntity {

  @Column
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column
  private Long size;

  @Column(nullable = false)
  private LocalDateTime publishedAt;

  @Column(nullable = false)
  private LocalDateTime lastModifiedAt;

  @OneToMany(mappedBy = "transcription")
  private List<Paragraph> paragraphs;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "file_id")
  private File file;
}

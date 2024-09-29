package project.transcription_application_v2.domain.transcription.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.transcription_application_v2.domain.BaseEntity;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;

@Entity
@Table(name = "transcriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transcription extends BaseEntity {

  @Column
  private String name;

  @Column
  private Long size;

  @Column(nullable = false)
  private LocalDateTime publishedAt = LocalDateTime.now();

  @Column(nullable = false)
  private LocalDateTime lastModifiedAt = LocalDateTime.now();

  @OneToMany(mappedBy = "transcription", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private List<Paragraph> paragraphs = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "file_id")
  private File file = null;
}

package project.transcription_application_v2.domain.file_meta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.transcription_application_v2.domain.BaseEntity;
import project.transcription_application_v2.domain.file.entity.File;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_meta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileMeta extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "text")
  private String description;

  @Column(nullable = false)
  private Long size;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private LocalDateTime date;

  @Column(nullable = false, columnDefinition = "text")
  private String downloadUrl;

  @Column(nullable = false, columnDefinition = "text")
  private String assemblyAiId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "file_id")
  private File file;
}

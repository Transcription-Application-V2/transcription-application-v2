package project.transcription_application_v2.domain.file.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.transcription_application_v2.domain.BaseEntity;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;
import project.transcription_application_v2.domain.transcription.entity.Transcription;
import project.transcription_application_v2.domain.user.entity.User;

@Entity
@Table(name = "files")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class File extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToOne(mappedBy = "file", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private FileMeta fileMeta;

  @OneToOne(mappedBy = "file", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private Transcription transcription;
}

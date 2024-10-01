package project.transcription_application_v2.domain.file_meta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;

import java.util.Optional;

@Repository
public interface FileMetaRepository extends JpaRepository<FileMeta, Long> {

  @Query("SELECT fm FROM FileMeta fm WHERE fm.file.id = :fileId")
  Optional<FileMeta> findByFileId(Long fileId);

  long countAllByDownloadUrl(String downloadUrl);

  long countAllByAssemblyAiId(String assemblyAiId);

  Optional<FileMeta> findFirstByDownloadUrl(String downloadUrl);
}

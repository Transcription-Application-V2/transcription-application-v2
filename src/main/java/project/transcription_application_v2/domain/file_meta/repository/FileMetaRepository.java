package project.transcription_application_v2.domain.file_meta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;

@Repository
public interface FileMetaRepository extends JpaRepository<FileMeta, Long> {
}

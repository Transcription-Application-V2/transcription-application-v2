package project.transcription_application_v2.domain.transcription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.transcription_application_v2.domain.transcription.entity.Transcription;

import java.util.Optional;

@Repository
public interface TranscriptionRepository extends JpaRepository<Transcription, Long> {

  @Query("SELECT t FROM Transcription t LEFT JOIN FETCH t.paragraphs WHERE t.file.id = :fileId")
  Optional<Transcription> findByFileIdWithParagraphs(@Param("fileId") Long fileId);
}

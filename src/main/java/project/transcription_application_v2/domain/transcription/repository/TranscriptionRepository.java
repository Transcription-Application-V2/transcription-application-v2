package project.transcription_application_v2.domain.transcription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.transcription_application_v2.domain.transcription.entity.Transcription;

@Repository
public interface TranscriptionRepository extends JpaRepository<Transcription, Long> {
}

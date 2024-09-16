package project.transcription_application_v2.domain.paragraph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {
}

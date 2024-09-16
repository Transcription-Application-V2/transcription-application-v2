package project.transcription_application_v2.domain.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.transcription_application_v2.domain.file.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}

package project.transcription_application_v2.domain.file.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import project.transcription_application_v2.domain.file.entity.File;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

  @Query("SELECT f FROM File f")
  Page<File> retrieveAllFiles(Pageable pageable);

  @Query("SELECT f FROM File f WHERE f.user.id = :userId")
  Page<File> retrieveAllFilesByUserId(@Param("userId") Long userId, Pageable pageable);

  List<File> findAllByUserId(Long userId);

  @Modifying
  @Query("DELETE FROM FileMeta fm WHERE fm.file.id = :fileId")
  void deleteFileMetaByFileId(@Param("fileId") Long fileId);

  @Modifying
  @Query("DELETE FROM Transcription t WHERE t.file.id = :fileId")
  void deleteTranscriptionByFileId(@Param("fileId") Long fileId);

  @Modifying
  @Query("DELETE FROM File f WHERE f.id = :fileId")
  void deleteFileById(@Param("fileId") Long fileId);

}

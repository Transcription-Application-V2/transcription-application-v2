package project.transcription_application_v2.domain.file_meta.dto;

import org.springframework.web.multipart.MultipartFile;

public record CreateFileMeta(
    MultipartFile file,
    String downloadUrl,
    String assemblyId,
    Long fileId) {
}

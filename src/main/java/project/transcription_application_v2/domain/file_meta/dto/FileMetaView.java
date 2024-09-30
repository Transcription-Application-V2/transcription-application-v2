package project.transcription_application_v2.domain.file_meta.dto;

import java.time.LocalDateTime;

public record FileMetaView(
    Long id,
    String name,
    Long size,
    String type,
    LocalDateTime date,
    String downloadUrl,
    String assemblyAiId
) {

}
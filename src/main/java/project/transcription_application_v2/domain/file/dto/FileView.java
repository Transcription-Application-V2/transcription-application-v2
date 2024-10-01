package project.transcription_application_v2.domain.file.dto;

import project.transcription_application_v2.domain.file_meta.dto.FileMetaView;
import project.transcription_application_v2.domain.transcription.dto.TranscriptionView;

public record FileView(
    Long id,
    FileMetaView fileMetaView,
    TranscriptionView transcriptionView
) {

}
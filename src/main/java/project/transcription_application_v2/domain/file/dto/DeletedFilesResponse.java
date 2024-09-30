package project.transcription_application_v2.domain.file.dto;

import java.util.List;

public record DeletedFilesResponse(List<String> deletedFiles, List<Long> failedIds) {
}
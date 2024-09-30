package project.transcription_application_v2.domain.file.dto;

import java.util.List;

public record UploadedFilesResponse(
    List<String> processedFiles,
    List<String> unprocessedFiles
) {

}
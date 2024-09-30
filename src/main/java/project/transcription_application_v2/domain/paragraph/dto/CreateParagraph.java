package project.transcription_application_v2.domain.paragraph.dto;

import com.assemblyai.api.resources.transcripts.types.Transcript;

public record CreateParagraph(
    Transcript transcript,
    Long transcriptionId
) {

}

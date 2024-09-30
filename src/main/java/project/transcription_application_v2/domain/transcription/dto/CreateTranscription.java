package project.transcription_application_v2.domain.transcription.dto;

import com.assemblyai.api.resources.transcripts.types.Transcript;

public record CreateTranscription(
    String name,
    Transcript transcript
) {

}

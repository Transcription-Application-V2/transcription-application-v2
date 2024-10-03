package project.transcription_application_v2.domain.paragraph.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateSpeakers(
    @NotNull(message = "File ID must not be null")
    @Positive(message = "File ID must be positive")
    Long fileId,

    @NotBlank(message = "Old speaker name must not be blank")
    @Size(max = 50, message = "Old speaker name must not exceed 50 characters")
    String oldSpeaker,

    @NotBlank(message = "New speaker name must not be blank")
    @Size(max = 50, message = "New speaker name must not exceed 50 characters")
    String newSpeaker
) {

}
package project.transcription_application_v2.domain.paragraph.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateParagraph(
    @NotBlank(message = "Speaker name must not be blank")
    @Size(max = 50, message = "Speaker name must not exceed 50 characters")
    String speaker,

    @NotNull(message = "Time must not be null")
    Long time,

    @NotBlank(message = "Text must not be blank")
    String text
) {

}

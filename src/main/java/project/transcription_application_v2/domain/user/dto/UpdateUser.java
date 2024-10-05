package project.transcription_application_v2.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUser(
    @Size(max = 255, message = "First name must be at most 255 characters.")
    @NotBlank(message = "First name is required.")
    String firstName,

    @Size(max = 255, message = "Last name must be at most 255 characters.")
    @NotBlank(message = "Last name is required.")
    String lastName
) {

}